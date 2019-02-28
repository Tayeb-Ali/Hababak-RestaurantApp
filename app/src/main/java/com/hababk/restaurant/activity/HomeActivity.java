package com.hababk.restaurant.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.UniversalPagerAdapter;
import com.hababk.restaurant.fragment.AccountFragment;
import com.hababk.restaurant.fragment.ItemsFragment;
import com.hababk.restaurant.fragment.OrderFragment;
import com.hababk.restaurant.fragment.OrdersFragment;
import com.hababk.restaurant.model.User;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.request.FcmTokenUpdateRequest;
import com.hababk.restaurant.network.response.ChefProfile;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;
import com.hababk.restaurant.view.NonSwipeableViewPager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView[] bottomViews = new TextView[4];
    private int[][] bottomViewRes = {{R.drawable.ic_list_24dp, R.drawable.ic_list_selected_24dp},
            {R.drawable.ic_directions_bike_24dp, R.drawable.ic_directions_bike_selected_24dp},
            {R.drawable.ic_restaurant_menu_24dp, R.drawable.ic_restaurant_menu_selected_24dp},
            {R.drawable.ic_person_24dp, R.drawable.ic_person_selected_24dp}};
    private NonSwipeableViewPager viewPager;

    private final String FRAG_TAG_ORDERS = "Orders";
    private final String FRAG_TAG_DELIVERY = "Deliveries";
    private final String FRAG_TAG_ITEMS = "Items";
    private final String FRAG_TAG_ACCOUNT = "Account";
    private UniversalPagerAdapter adapter;
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        Helper.refreshSettings(sharedPreferenceUtil);

        ChefProfile chefProfile = Helper.getChefDetails(sharedPreferenceUtil);
        if (chefProfile == null || TextUtils.isEmpty(chefProfile.getName())) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

        initUi();
        setupViewPager();
        Helper.preLoadCategories(sharedPreferenceUtil);
        updateFcmToken();
    }

    private void setupViewPager() {
        adapter = new UniversalPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OrdersFragment(), FRAG_TAG_ORDERS);
        adapter.addFrag(OrderFragment.newStatusInstance(OrderFragment.DELIVERIES_ORDERS), FRAG_TAG_DELIVERY);
        adapter.addFrag(new ItemsFragment(), FRAG_TAG_ITEMS);
        adapter.addFrag(new AccountFragment(), FRAG_TAG_ACCOUNT);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(FRAG_TAG_ORDERS);
        viewPager = findViewById(R.id.viewPager);
        bottomViews[0] = findViewById(R.id.bottomNavOption1);
        bottomViews[1] = findViewById(R.id.bottomNavOption2);
        bottomViews[2] = findViewById(R.id.bottomNavOption3);
        bottomViews[3] = findViewById(R.id.bottomNavOption4);
        for (TextView bottomView : bottomViews)
            bottomView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0)
            setBottomBarSelection(0);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottomNavOption1:
                setBottomBarSelection(0);
                break;
            case R.id.bottomNavOption2:
                setBottomBarSelection(1);
                break;
            case R.id.bottomNavOption3:
                setBottomBarSelection(2);
                break;
            case R.id.bottomNavOption4:
                setBottomBarSelection(3);
                break;
        }
    }

    private void setBottomBarSelection(final int selectedIndex) {
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(selectedIndex);
                getSupportActionBar().setTitle(adapter.getPageTitle(selectedIndex));
            }
        });
        for (int i = 0; i < 4; i++) {
            bottomViews[i].setTextColor(ContextCompat.getColor(this, i == selectedIndex ? R.color.colorAccent : R.color.grayDark));
            bottomViews[i].setBackgroundResource(i == selectedIndex ? R.drawable.bottom_bar_item_selected : android.R.color.white);
            bottomViews[i].setCompoundDrawablesWithIntrinsicBounds(0, bottomViewRes[i][i == selectedIndex ? 1 : 0], 0, 0);
        }
    }

    private void updateFcmToken() {
        ApiUtils.getClient().create(ChefStoreService.class).updateFcmToken(Helper.getApiToken(sharedPreferenceUtil), new FcmTokenUpdateRequest(FirebaseInstanceId.getInstance().getToken())).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("FcmTokenUpdate", String.valueOf(response.isSuccessful()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("FcmTokenUpdate", t.getMessage());
            }
        });
    }
}
