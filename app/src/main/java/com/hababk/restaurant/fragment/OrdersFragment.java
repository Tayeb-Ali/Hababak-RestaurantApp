package com.hababk.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.ViewPagerAdapter;
import com.hababk.restaurant.utils.Constants;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

/**
 * Created by Tayeb-Ali on 1/28/2018.
 */

public class OrdersFragment extends Fragment {
    private TabLayout mOrderTabs;
    private ViewPager mOrderViewPager;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ViewPagerAdapter adapter;

    public OrdersFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        mOrderTabs = view.findViewById(R.id.order_tabs);
        mOrderViewPager = view.findViewById(R.id.order_viewpager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferenceUtil.getBooleanPreference(Constants.KEY_REFRESH_ORDERS, false)) {
            sharedPreferenceUtil.setBooleanPreference(Constants.KEY_REFRESH_ORDERS, false);
            if (adapter != null && adapter.getCount() == 2) {
                ((OrderFragment) adapter.getItem(0)).onSwipeRefresh();
                ((OrderFragment) adapter.getItem(1)).onSwipeRefresh();
                Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setUpViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(OrderFragment.newStatusInstance(OrderFragment.ACTIVE_ORDERS), getString(R.string.new_orders));
        adapter.addFragment(OrderFragment.newStatusInstance(OrderFragment.COMPLETE_ORDERS), getString(R.string.past_orders));
        mOrderViewPager.setAdapter(adapter);
        mOrderTabs.setupWithViewPager(mOrderViewPager);
        mOrderViewPager.post(new Runnable() {
            @Override
            public void run() {
                mOrderViewPager.setCurrentItem(0);
            }
        });
    }
}
