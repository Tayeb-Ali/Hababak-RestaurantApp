package com.hababk.appstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hababk.appstore.R;
import com.hababk.appstore.activity.AddItemActivity;
import com.hababk.appstore.adapter.UniversalPagerAdapter;
import com.hababk.appstore.utils.Constants;
import com.hababk.appstore.utils.SharedPreferenceUtil;

/**
 * Created by user on 1/28/2018.
 */

public class ItemsFragment extends Fragment {
    TabLayout mItemTabs;
    ViewPager mItemViewPager;
    FloatingActionButton mItemAddFab;
    private SharedPreferenceUtil sharedPreferences;
    private UniversalPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = new SharedPreferenceUtil(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        mItemTabs = view.findViewById(R.id.item_tabs);
        mItemViewPager = view.findViewById(R.id.item_viewpager);
        mItemAddFab = view.findViewById(R.id.item_add_fab);
        mItemAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddItemActivity.class));
            }
        });
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
        if (sharedPreferences.getBooleanPreference(Constants.KEY_REFRESH_ITEMS, false)) {
            if (adapter != null && adapter.getCount() == 2) {
                ((MenuItemsFragment) adapter.getItem(0)).onSwipeRefresh();
                ((MenuItemsFragment) adapter.getItem(1)).onSwipeRefresh();
                Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
            }
            sharedPreferences.setBooleanPreference(Constants.KEY_REFRESH_ITEMS, false);
        }
    }

    public void setUpViewPager() {
        adapter = new UniversalPagerAdapter(getChildFragmentManager());
        adapter.addFrag(MenuItemsFragment.newInstance("approved"), "Approved Items");
        adapter.addFrag(MenuItemsFragment.newInstance("pending"), "Pending Items");
        mItemViewPager.setAdapter(adapter);
        mItemTabs.setupWithViewPager(mItemViewPager);
        mItemViewPager.post(new Runnable() {
            @Override
            public void run() {
                mItemViewPager.setCurrentItem(0);
            }
        });
    }
}
