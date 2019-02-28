package com.hababk.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.hababk.restaurant.activity.EarningsActivity;
import com.hababk.restaurant.adapter.HistoryAdapter;
import com.hababk.restaurant.network.response.Earning;
import com.hababk.restaurant.network.response.EarningResponse;
import com.hababk.restaurant.utils.Helper;

import java.util.ArrayList;

public class EarningHistoryFragment extends BaseRecyclerFragment {
    private ArrayList<Earning> orderItems;
    private HistoryAdapter adapter;
    private int pageNo = 1;
    private EarningsActivity.EarningFragmentInteractor earningFragmentInteractor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderItems = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), orderItems);
        recyclerView.setAdapter(adapter);
        loadItems();
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        earningFragmentInteractor = null;
    }

    private void loadItems() {
        if (earningFragmentInteractor != null) {
            isLoading = true;
            earningFragmentInteractor.loadItems(pageNo);
        }
    }

    @Override
    void onRecyclerViewScrolled() {
        pageNo++;
        loadItems();
    }

    @Override
    void onSwipeRefresh() {
        pageNo = 1;
        orderItems.clear();
        adapter.notifyDataSetChanged();
        allDone = false;
        loadItems();
        emptyViewContainer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setData(EarningResponse body) {
        isLoading = false;
        if (swipeRefresh.isRefreshing())
            swipeRefresh.setRefreshing(false);

        if (pageNo == 1 && body != null && body.getEarnings().getData() == null) {
            int posTillToday = -1;
            for (int i = 0; i < body.getEarnings().getData().size(); i++) {
                if (Helper.isToday(body.getEarnings().getData().get(i).getUpdated_at())) {
                    posTillToday = i;
                } else {
                    break;
                }
            }
            if (posTillToday != -1) {
                body.getEarnings().getData().add(0, new Earning("Today"));
                if (body.getEarnings().getData().size() > 2)
                    body.getEarnings().getData().add(posTillToday + 2, new Earning("Older"));
            }
        }

        allDone = body != null && (body.getEarnings().getData() == null || body.getEarnings().getData().isEmpty());
        if (body == null || body.getEarnings().getData() == null || body.getEarnings().getData().isEmpty()) {
            if (orderItems.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyViewContainer.setVisibility(View.VISIBLE);
            }
        } else {
            orderItems.addAll(body.getEarnings().getData());
            adapter.notifyDataSetChanged();
        }
    }

    public static EarningHistoryFragment newInstance(EarningsActivity.EarningFragmentInteractor earningFragmentInteractor) {
        EarningHistoryFragment earningHistoryFragment = new EarningHistoryFragment();
        earningHistoryFragment.earningFragmentInteractor = earningFragmentInteractor;
        return earningHistoryFragment;
    }
}
