package com.hababk.restaurant.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.ReviewAdapter;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.response.BaseListModel;
import com.hababk.restaurant.network.response.Review;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private View emptyView;
    private ImageView emptyViewImage;
    private TextView emptyViewText;
    private boolean isLoading, allDone;
    private ChefStoreService service;
    private SharedPreferenceUtil sharedPreferences;
    private int pageNo = 1;

    private Callback<BaseListModel<Review>> callback = new Callback<BaseListModel<Review>>() {
        @Override
        public void onResponse(Call<BaseListModel<Review>> call, Response<BaseListModel<Review>> response) {
            if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            if (response.isSuccessful() && recyclerView != null) {
                allDone = response.body().getData().isEmpty();
                reviews.addAll(response.body().getData());
                reviewAdapter.notifyDataSetChanged();
            }
            if (reviews.isEmpty() && emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
                Glide.with(ReviewActivity.this).load(R.drawable.placeholder_review).into(emptyViewImage);
                emptyViewText.setText("No reviews found at the moment");
            }
        }

        @Override
        public void onFailure(Call<BaseListModel<Review>> call, Throwable t) {
            if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            if (reviews.isEmpty() && emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initUi();
        service = ApiUtils.getClient().create(ChefStoreService.class);
        sharedPreferences = new SharedPreferenceUtil(this);
        setupReviewsRecycler();
        loadReviews();
    }

    private void loadReviews() {
        swipeRefresh.setRefreshing(true);
        service.getReviewsMine(Helper.getApiToken(sharedPreferences), pageNo).enqueue(callback);
    }

    private void setupReviewsRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // init
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                RecyclerView.Adapter adapter = recyclerView.getAdapter();

                if (layoutManager.getChildCount() > 0) {
                    // Calculations..
                    int indexOfLastItemViewVisible = layoutManager.getChildCount() - 1;
                    View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
                    int adapterPosition = layoutManager.getPosition(lastItemViewVisible);
                    boolean isLastItemVisible = (adapterPosition == adapter.getItemCount() - 1);
                    // check
                    if (isLastItemVisible && !isLoading && !allDone) {
                        isLoading = true;
                        pageNo++;
                        loadReviews();
                    }
                }
            }
        });
        reviewAdapter = new ReviewAdapter(this, reviews);
        recyclerView.setAdapter(reviewAdapter);
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Reviews");
        }
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyViewContainer);
        emptyViewImage = findViewById(R.id.emptyViewImage);
        emptyViewText = findViewById(R.id.emptyViewText);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                reviews.clear();
                reviewAdapter.notifyDataSetChanged();
                loadReviews();
            }
        });
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

}
