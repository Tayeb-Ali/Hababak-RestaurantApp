package com.hababk.appstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hababk.appstore.R;
import com.hababk.appstore.activity.AddItemActivity;
import com.hababk.appstore.network.ApiUtils;
import com.hababk.appstore.network.ChefStoreService;
import com.hababk.appstore.network.request.MenuItemCreateRequest;
import com.hababk.appstore.network.response.MenuItem;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by a_man on 14-03-2018.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    private final String menuItemStatus;
    private ArrayList<MenuItem> dataList;
    private Context context;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ChefStoreService storeService;

    public MenuItemAdapter(ArrayList<MenuItem> dataList, Context context, String menuItemStatus) {
        this.dataList = dataList;
        this.context = context;
        this.menuItemStatus = menuItemStatus;
        this.sharedPreferenceUtil = new SharedPreferenceUtil(context);
        this.storeService = ApiUtils.getClient().create(ChefStoreService.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(dataList.get(position));
        holder.toggleAble = true;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName, itemType, itemPrice, itemStatus, availableTitle;
        private SwitchCompat itemAvailable;
        private boolean toggleAble;

        MyViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemType = itemView.findViewById(R.id.itemType);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            itemAvailable = itemView.findViewById(R.id.itemAvailable);
            availableTitle = itemView.findViewById(R.id.availableTitle);

            itemAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (toggleAble) markAvailability();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != -1)
                        context.startActivity(AddItemActivity.newIntent(context, dataList.get(pos)));
                }
            });
        }

        public void setData(MenuItem menuItem) {
            Glide.with(context).load(menuItem.getImage_url()).apply(new RequestOptions().placeholder(R.drawable.placeholder_food)).into(itemImage);
            itemName.setText(menuItem.getTitle());
            itemType.setText(menuItem.getIs_non_veg() == 1 ? "Non-Veg Food" : "Veg Food");
            itemPrice.setText(String.valueOf(menuItem.getPrice()));
            itemAvailable.setChecked(menuItem.getIs_available() == 1);
            itemStatus.setText(menuItem.getStatus());
            itemStatus.setTextColor(context.getResources().getColor(menuItem.getStatus().equalsIgnoreCase("rejected") ? R.color.colorAccent : R.color.colorPrimary));

            availableTitle.setText(menuItemStatus.equals("pending") ? "Status" : "Available");
            itemAvailable.setVisibility(menuItemStatus.equals("pending") ? View.GONE : View.VISIBLE);
            itemStatus.setVisibility(menuItemStatus.equals("pending") ? View.VISIBLE : View.GONE);
        }

        private void markAvailability() {
            int pos = getAdapterPosition();
            if (pos != -1) {
                itemAvailable.setClickable(false);
                MenuItem item = dataList.get(pos);
                item.setIs_available(itemAvailable.isChecked() ? 1 : 0);
                storeService.updateMenuItem(Helper.getApiToken(sharedPreferenceUtil), MenuItemCreateRequest.newInstance(item), item.getId()).enqueue(new Callback<MenuItem>() {
                    @Override
                    public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                        itemAvailable.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<MenuItem> call, Throwable t) {
                        itemAvailable.setClickable(true);
                    }
                });
            }
        }
    }
}
