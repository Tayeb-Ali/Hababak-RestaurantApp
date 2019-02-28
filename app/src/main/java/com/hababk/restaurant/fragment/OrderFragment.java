package com.hababk.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.OrderAdapter;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.response.BaseListModel;
import com.hababk.restaurant.network.response.Order;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 2/1/2018.
 */

public class OrderFragment extends BaseRecyclerFragment {
    public static final String ACTIVE_ORDERS = "active";
    public static final String COMPLETE_ORDERS = "complete";
    public static final String DELIVERIES_ORDERS = "deliveries";

    private SharedPreferenceUtil sharedPreferenceUtil;
    private ChefStoreService chefService;
    private ArrayList<Order> orderItems = new ArrayList<>();
    private OrderAdapter adapter;
    private int active_orders, deliveries, pageNo = 1;
    private String orderType, emptyMessage;

    private Callback<BaseListModel<Order>> callBack = new Callback<BaseListModel<Order>>() {
        @Override
        public void onResponse(Call<BaseListModel<Order>> call, Response<BaseListModel<Order>> response) {
            isLoading = false;
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (response.isSuccessful()) {
                BaseListModel<Order> menuItemResponse = response.body();
                if (menuItemResponse.getData() == null || menuItemResponse.getData().isEmpty()) {
                    if (orderItems.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyViewContainer.setVisibility(View.VISIBLE);
                    }
                    allDone = true;
                } else {
                    orderItems.addAll(menuItemResponse.getData());
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (orderItems.isEmpty()) {
                    emptyViewContainer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onFailure(Call<BaseListModel<Order>> call, Throwable t) {
            isLoading = false;
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (orderItems.isEmpty()) {
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

        switch (orderType) {
            case ACTIVE_ORDERS:
                emptyMessage = getString(R.string.empty_order_active);
                break;
            case DELIVERIES_ORDERS:
                emptyMessage = getString(R.string.empty_order_delivery);
                break;
            case COMPLETE_ORDERS:
                emptyMessage = getString(R.string.empty_order_complete);
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OrderAdapter(getContext(), orderItems);
        recyclerView.setAdapter(adapter);
        loadItems();
        swipeRefresh.setRefreshing(true);
        emptyViewText.setText(emptyMessage);
        Glide.with(getContext()).load(R.drawable.placeholder_food).into(emptyViewImage);
    }

    private void loadItems() {
        isLoading = true;
        String status = orderType.equals(COMPLETE_ORDERS) ? orderType : null;
        chefService.getOrders(Helper.getApiToken(sharedPreferenceUtil), status, active_orders, deliveries, pageNo).enqueue(callBack);
    }

    @Override
    void onRecyclerViewScrolled() {
        pageNo++;
        loadItems();
    }

    @Override
    void onSwipeRefresh() {
        if (recyclerView != null) {
            pageNo = 1;
            orderItems.clear();
            adapter.notifyDataSetChanged();
            allDone = false;
            loadItems();
            emptyViewContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public static OrderFragment newStatusInstance(String orderType) {
        OrderFragment fragment = new OrderFragment();
        fragment.orderType = orderType;
        switch (orderType) {
            case ACTIVE_ORDERS:
                fragment.active_orders = 1;
                fragment.deliveries = 0;
                break;
            case DELIVERIES_ORDERS:
                fragment.active_orders = 0;
                fragment.deliveries = 1;
                break;
            case COMPLETE_ORDERS:
                fragment.active_orders = 0;
                fragment.deliveries = 0;
                break;
        }
        return fragment;
    }
}
