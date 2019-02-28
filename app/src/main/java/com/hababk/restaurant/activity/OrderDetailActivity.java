package com.hababk.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hababk.restaurant.R;
import com.hababk.restaurant.adapter.OrderItemAdapter;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.response.Order;
import com.hababk.restaurant.utils.Constants;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.text.DecimalFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private static String DATA_ORDER = "Order";
    private static String DATA_ORDER_ID = "OrderId";
    private RecyclerView mItemRv;
    private TextView mCancelOrderTv, mAcceptOrderTv, mNameTv, mPaymentMethod2Tv, mAddressTv, personNameTv, priceTv, paymentMethodTv, orderStatusTv, orderNumberTv, orderDateTv, errorText;
    private ImageView mHomeIv;
    private ProgressBar updateProgress;

    private String PRICE_UNIT, toUpdate = null;
    private Order order;
    private ChefStoreService storeService;
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        order = getIntent().getParcelableExtra(DATA_ORDER);
        String orderId = getIntent().getStringExtra(DATA_ORDER_ID);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        storeService = ApiUtils.getClient().create(ChefStoreService.class);
        String currency = Helper.getSetting(sharedPreferenceUtil, "currency");
        PRICE_UNIT = TextUtils.isEmpty(currency) ? "" : " " + currency;

        initUi();
        if (order != null)
            setDetails();
        else if (orderId != null)
            getOrder(orderId);
    }

    private void getOrder(String orderId) {
        storeService.getOrderById(Helper.getApiToken(sharedPreferenceUtil), orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    order = response.body();
                    if (order != null)
                        setDetails();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void setDetails() {
        findViewById(R.id.orderActionContainer).setVisibility(View.VISIBLE);
        setButtons();

        personNameTv.setText(order.getUser().getName());
        priceTv.setText(new DecimalFormat("###.##").format(order.getSubtotal()) + PRICE_UNIT);
        paymentMethodTv.setText("Cod");
        orderNumberTv.setText("Order #" + order.getId());
        orderDateTv.setText("Ordered on " + Helper.getReadableDateTime(order.getCreated_at()));

        mItemRv.setLayoutManager(new LinearLayoutManager(this));
        mItemRv.setAdapter(new OrderItemAdapter(this, order.getOrderitems()));

        mNameTv.setText(order.getUser().getName());
        mPaymentMethod2Tv.setText("Cod");
        mAddressTv.setText(order.getAddress().getAddress());
    }

    private void setButtons() {
        mCancelOrderTv.setVisibility(View.GONE);
        mAcceptOrderTv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        switch (order.getStatus()) {
            case "new":
                mCancelOrderTv.setVisibility(View.VISIBLE);
                mAcceptOrderTv.setText("Accept Order");
                break;
            case "accepted":
                mAcceptOrderTv.setText("Dispatch Order");
                break;
            case "rejected":
                mAcceptOrderTv.setText("Order Rejected");
                mAcceptOrderTv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case "dispatched":
                mAcceptOrderTv.setText("Order Dispatched");
                break;
        }
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Order info");
        }
        findViewById(R.id.new_order_status_tv).setVisibility(View.GONE);

        mItemRv = findViewById(R.id.order_info_items_recyclerview);
        mCancelOrderTv = findViewById(R.id.order_info_cancel_order_tv);
        mAcceptOrderTv = findViewById(R.id.order_info_accept_order_tv);
        mNameTv = findViewById(R.id.order_info_name_tv);
        mPaymentMethod2Tv = findViewById(R.id.order_info_payment_method2_tv);
        mAddressTv = findViewById(R.id.order_info_address_tv);
        mHomeIv = findViewById(R.id.order_info_home_iv);
        updateProgress = findViewById(R.id.updateProgress);

        personNameTv = findViewById(R.id.new_order_person_name_tv);
        priceTv = findViewById(R.id.new_order_price_tv);
        paymentMethodTv = findViewById(R.id.new_order_payment_method_tv);
        orderStatusTv = findViewById(R.id.new_order_status_tv);
        orderNumberTv = findViewById(R.id.new_order_order_text_tv);
        orderDateTv = findViewById(R.id.new_order_dispatch_text_tv);
        errorText = findViewById(R.id.errorText);

        findViewById(R.id.order_info_open_map_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInMap();
            }
        });
//        if (order!=null && order.getStatus().equals("rejected") || order.getStatus().equals("dispatched")) {
//            findViewById(R.id.orderActionContainer).setVisibility(View.GONE);
//        }
        mCancelOrderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null && order.getStatus().equals("new")) updateOrderStatus("rejected");
            }
        });
        mAcceptOrderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null) {
                    switch (order.getStatus()) {
                        case "new":
                            toUpdate = "accepted";
                            break;
                        case "accepted":
                            toUpdate = "dispatched";
                            break;
                    }
                    if (toUpdate != null)
                        updateOrderStatus(toUpdate);
                }
            }
        });
        findViewById(R.id.orderActionContainer).setVisibility(View.GONE);
    }

    private void openInMap() {
        if (order != null && order.getAddress() != null && order.getAddress().getLatitude() != null && order.getAddress().getLongitude() != null)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://maps.google.com/maps?q=loc:%s,%s", order.getAddress().getLatitude(), order.getAddress().getLongitude()))));
    }

    private void updateOrderStatus(String status) {
        HashMap<String, String> updateRequest = new HashMap<>();
        updateRequest.put("status", status);

        updateProgress.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        storeService.updateOrderStatus(Helper.getApiToken(sharedPreferenceUtil), updateRequest, order.getId()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                updateProgress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    order = response.body();
                    sharedPreferenceUtil.setBooleanPreference(Constants.KEY_REFRESH_ORDERS, true);
                    Toast.makeText(OrderDetailActivity.this, "Order updated", Toast.LENGTH_SHORT).show();
                    setButtons();
                    if (toUpdate != null && toUpdate.equals("dispatched")) {
                        finish();
                    }
                } else if (toUpdate != null && toUpdate.equals("dispatched")) {
                    if (response.code() == 422) {
                        errorText.setVisibility(View.VISIBLE);
                        updateProgress.setVisibility(View.GONE);
                        errorText.setText(R.string.delivery_guy_assigned);
                    } else if (response.code() == 404) {
                        updateProgress.setVisibility(View.GONE);
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(R.string.delivery_guy_not_assigned);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Something went wrong updating status", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Something went wrong updating status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                updateProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(OrderDetailActivity.this, "Something went wrong updating status", Toast.LENGTH_SHORT).show();
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

    public static Intent newInstance(Context context, String order_id) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(DATA_ORDER_ID, order_id);
        return intent;
    }

    public static Intent newInstance(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(DATA_ORDER, order);
        return intent;
    }
}
