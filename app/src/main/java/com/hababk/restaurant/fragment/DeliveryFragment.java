package com.hababk.restaurant.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.hababk.restaurant.adapter.MenuItemAdapter;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.response.BaseListModel;
import com.hababk.restaurant.network.response.MenuItem;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tayeb-Ali on 1/28/2018.
 */

public class DeliveryFragment extends BaseRecyclerFragment {
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ChefStoreService chefService;
    private ArrayList<MenuItem> menuItems;
    private MenuItemAdapter adapter;
    private int pageNo = 1;
    private String menuItemStatus;
    private String orderType;

    private Callback<BaseListModel<MenuItem>> callBack = new Callback<BaseListModel<MenuItem>>() {
        @Override
        public void onResponse(Call<BaseListModel<MenuItem>> call, Response<BaseListModel<MenuItem>> response) {
            isLoading = false;
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (response.isSuccessful()) {
                BaseListModel<MenuItem> menuItemResponse = response.body();
                if (menuItemResponse.getData() == null || menuItemResponse.getData().isEmpty()) {
                    if (menuItems.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyViewContainer.setVisibility(View.VISIBLE);
                    }
                    allDone = true;
                } else {
                    menuItems.addAll(menuItemResponse.getData());
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (menuItems.isEmpty()) {
                    emptyViewContainer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<BaseListModel<MenuItem>> call, Throwable t) {
            isLoading = false;
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (menuItems.isEmpty()) {
                emptyViewContainer.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chefService = ApiUtils.getClient().create(ChefStoreService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuItems = new ArrayList<>();
        adapter = new MenuItemAdapter(menuItems, getContext(), menuItemStatus);
        recyclerView.setAdapter(adapter);
        loadItems();
        swipeRefresh.setRefreshing(true);
    }

    private void loadItems() {
        isLoading = true;
        //chefService.getMenuItems(Helper.getApiToken(sharedPreferenceUtil), menuItemStatus, pageNo).enqueue(callBack);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                emptyViewContainer.setVisibility(View.VISIBLE);
                allDone = true;
            }
        }, 200);
    }

    @Override
    void onRecyclerViewScrolled() {
        pageNo++;
        loadItems();
    }

    @Override
    void onSwipeRefresh() {
        pageNo = 1;
        menuItems.clear();
        adapter.notifyDataSetChanged();
        allDone = false;
        loadItems();
        emptyViewContainer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
