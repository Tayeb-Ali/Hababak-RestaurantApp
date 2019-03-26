package com.hababk.restaurant.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.UniversalPagerAdapter;
import com.hababk.restaurant.fragment.AccountFragment;
import com.hababk.restaurant.fragment.ItemsFragment;
import com.hababk.restaurant.fragment.OrderFragment;
import com.hababk.restaurant.fragment.OrdersFragment;
import com.hababk.restaurant.network.response.ChefProfile;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;
import com.hababk.restaurant.view.NonSwipeableViewPager;

//import com.google.firebase.iid.FirebaseInstanceId;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView[] bottomViews = new TextView[4];
    private int[][] bottomViewRes = {{R.drawable.ic_list_24dp, R.drawable.ic_list_selected_24dp},
            {R.drawable.ic_directions_bike_24dp, R.drawable.ic_directions_bike_selected_24dp},
            {R.drawable.ic_restaurant_menu_24dp, R.drawable.ic_restaurant_menu_selected_24dp},
            {R.drawable.ic_person_24dp, R.drawable.ic_person_selected_24dp}};
    private NonSwipeableViewPager viewPager;

    private final String FRAG_TAG_ORDERS = "الطلبات";
    private final String FRAG_TAG_DELIVERY = "التوصيل";
    private final String FRAG_TAG_ITEMS = "العناصر";
    private final String FRAG_TAG_ACCOUNT = "الحساب";
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
//        updateFcmToken();
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

//    private void updateFcmToken() {
//        ApiUtils.getClient().create(ChefStoreService.class).updateFcmToken(Helper.getApiToken(sharedPreferenceUtil), new FcmTokenUpdateRequest(FirebaseInstanceId.getInstance().getToken())).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.e("FcmTokenUpdate", String.valueOf(response.isSuccessful()));
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e("FcmTokenUpdate", t.getMessage());
//            }
//        });
//    }

    /**
     * Created by Arbab on 3/22/2019.
     */

    public static class GPSTracker extends Service implements LocationListener {


        private final Context context;

        boolean isGPSEnabled =false;
        boolean isNetworkEnabled =false;
        boolean canGetLocation = false;

        Location location;
        protected LocationManager locationManager;

        public GPSTracker(Context context){
            this.context=context;
        }

        //Create a GetLocation Method //
        public  Location getLocation(){
            try{

                locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
                isNetworkEnabled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

                if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ){

                    if(isGPSEnabled){
                        if(location==null){
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,10,this);
                            if(locationManager!=null){
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }
                        }
                    }
                    // if lcoation is not found from GPS than it will found from network //
                    if(location==null){
                        if(isNetworkEnabled){

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,10,this);
                            if(locationManager!=null){
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }

                        }
                    }

                }

            }catch(Exception ex){

            }
            return  location;
        }

        // followings are the default method if we imlement LocationListener //
        public void onLocationChanged(Location location){

        }

        public void onStatusChanged(String Provider, int status, Bundle extras){

        }
        public void onProviderEnabled(String Provider){

        }
        public void onProviderDisabled(String Provider){

        }
        public IBinder onBind(Intent arg0){
            return null;
        }

    }
}
