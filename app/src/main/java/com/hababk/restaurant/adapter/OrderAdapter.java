package com.hababk.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hababk.restaurant.R;
import com.hababk.restaurant.activity.OrderDetailActivity;
import com.hababk.restaurant.network.response.Order;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by user on 2/1/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.NewOrderHolder> {
    private Context context;
    private ArrayList<Order> dataList;
    private String PRICE_UNIT;

    public OrderAdapter(Context context, ArrayList<Order> orderItems) {
        this.context = context;
        this.dataList = orderItems;
        String currency = Helper.getSetting(new SharedPreferenceUtil(context), "currency");
        PRICE_UNIT = TextUtils.isEmpty(currency) ? "" : " " + currency;
    }

    @NonNull
    @Override
    public NewOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewOrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderHolder holder, int position) {
        holder.personNameTv.setText(dataList.get(position).getUser().getName());
        holder.priceTv.setText(holder.decimalFormat.format(dataList.get(position).getSubtotal()) + PRICE_UNIT);
        holder.paymentMethodTv.setText("Cod");
        holder.orderNumberTv.setText("Order #" + dataList.get(position).getId());
        holder.orderDateTv.setText("Ordered on " + Helper.getReadableDateTime(dataList.get(position).getCreated_at()));
        holder.orderStatusFlag = dataList.get(position).getStatus();
        holder.orderStatusTv.setText(holder.orderStatusFlag.equals("new") ? "View Order" : holder.orderStatusFlag.substring(0, 1).toUpperCase() + holder.orderStatusFlag.substring(1));
        holder.orderStatusTv.setTextColor(context.getResources().getColor(holder.orderStatusFlag.equals("rejected") ? R.color.colorAccent : R.color.colorPrimary));
        holder.orderStatusTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, holder.orderStatusFlag.equals("rejected") ? R.drawable.ic_keyboard_arrow_right_accent_24dp : R.drawable.ic_keyboard_arrow_right_primary_24dp, 0);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class NewOrderHolder extends RecyclerView.ViewHolder {
        TextView personNameTv;
        TextView priceTv;
        TextView paymentMethodTv;
        TextView orderStatusTv;
        LinearLayout mItemLayout;
        TextView orderNumberTv;
        TextView orderDateTv;

        DecimalFormat decimalFormat;

        String orderStatusFlag = "new";

        NewOrderHolder(View itemView) {
            super(itemView);
            decimalFormat = new DecimalFormat("###.##");
            personNameTv = itemView.findViewById(R.id.new_order_person_name_tv);
            priceTv = itemView.findViewById(R.id.new_order_price_tv);
            paymentMethodTv = itemView.findViewById(R.id.new_order_payment_method_tv);
            orderStatusTv = itemView.findViewById(R.id.new_order_status_tv);
            mItemLayout = itemView.findViewById(R.id.new_order_item_layout);
            orderNumberTv = itemView.findViewById(R.id.new_order_order_text_tv);
            orderDateTv = itemView.findViewById(R.id.new_order_dispatch_text_tv);

            mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != -1) {
                        context.startActivity(OrderDetailActivity.newInstance(context, dataList.get(pos)));
                    }
                }
            });
        }
    }
}
