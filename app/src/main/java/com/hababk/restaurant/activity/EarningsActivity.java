package com.hababk.appstore.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hababk.appstore.R;
import com.hababk.appstore.adapter.ViewPagerAdapter;
import com.hababk.appstore.fragment.EarningHistoryFragment;
import com.hababk.appstore.fragment.EarningTotalFragment;
import com.hababk.appstore.network.ApiUtils;
import com.hababk.appstore.network.ChefStoreService;
import com.hababk.appstore.network.response.EarningResponse;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsActivity extends AppCompatActivity {
    private TabLayout mEarningsTabs;
    private ViewPager mEarningsViewPager;

    private ChefStoreService chefService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private EarningTotalFragment totalEarningFragment;
    private EarningHistoryFragment historyEarningFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        chefService = ApiUtils.getClient().create(ChefStoreService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Earnings");
        }
        mEarningsTabs = findViewById(R.id.earnings_tabs);
        mEarningsViewPager = findViewById(R.id.earnings_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        totalEarningFragment = new EarningTotalFragment();
        historyEarningFragment = EarningHistoryFragment.newInstance(new EarningFragmentInteractor() {
            @Override
            public void loadItems(int pageNo) {
                getEarnings(pageNo);
            }
        });
        adapter.addFragment(totalEarningFragment, "Total");
        adapter.addFragment(historyEarningFragment, "History");
        mEarningsViewPager.setAdapter(adapter);
        mEarningsTabs.setupWithViewPager(mEarningsViewPager);
    }

    private void getEarnings(int pageNo) {
        chefService.getEarnings(Helper.getApiToken(sharedPreferenceUtil), pageNo).enqueue(new Callback<EarningResponse>() {
            @Override
            public void onResponse(Call<EarningResponse> call, Response<EarningResponse> response) {
                if (response.isSuccessful()) {
                    setData(response.body());
                } else {
                    setData(null);
                }
            }

            @Override
            public void onFailure(Call<EarningResponse> call, Throwable t) {
                setData(null);
            }
        });
    }

    private void setData(EarningResponse body) {
        if (totalEarningFragment != null) totalEarningFragment.setData(body);
        if (historyEarningFragment != null) historyEarningFragment.setData(body);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface EarningFragmentInteractor {
        void loadItems(int pageNo);
    }

}
