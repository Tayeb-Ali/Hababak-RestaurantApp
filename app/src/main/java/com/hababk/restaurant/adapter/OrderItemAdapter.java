package com.hababk.appstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hababk.appstore.R;
import com.hababk.appstore.network.response.RequestItem;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by user on 2/5/2018.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {
    public Context context;
    private ArrayList<RequestItem> dataList;
    private String X_SEPRATOR = " X ";
    private String PRICE_UNIT;

    public OrderItemAdapter(Context context, ArrayList<RequestItem> orderitems) {
        this.context = context;
        this.dataList = orderitems;
        String currency = Helper.getSetting(new SharedPreferenceUtil(context), "currency");
        PRICE_UNIT = TextUtils.isEmpty(currency) ? "" : " " + currency;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemHolder(LayoutInflater.from(context).inflate(R.layout.order_items_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        holder.mNameTv.setText(dataList.get(position).getMenuitem().getTitle());
        holder.mPriceQuantityTv.setText(dataList.get(position).getQuantity() + X_SEPRATOR + new DecimalFormat("###.##").format(dataList.get(position).getMenuitem().getPrice()) + PRICE_UNIT);
        holder.mSubTotalTv.setText(dataList.get(position).getTotal() + PRICE_UNIT);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        TextView mSubTotalTv;
        TextView mPriceQuantityTv;


        public OrderItemHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.order_item_name_tv);
            mSubTotalTv = itemView.findViewById(R.id.order_item_sub_total_tv);
            mPriceQuantityTv = itemView.findViewById(R.id.order_item_price_quantity_tv);
        }
    }
}
