package com.hababk.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hababk.restaurant.R;
import com.hababk.restaurant.network.response.MenuItemCategory;

import java.util.ArrayList;

/**
 * Created by a_man on 15-03-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final ArrayList<MenuItemCategory> dataList;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<MenuItemCategory> categories) {
        this.context = context;
        this.dataList = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ImageView categorySelected;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categorySelected = itemView.findViewById(R.id.categorySelected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    dataList.get(pos).setSelected(!dataList.get(pos).isSelected());
                    notifyItemChanged(pos);
                }
            });
        }

        public void setData(MenuItemCategory menuItemCategory) {
            categoryName.setText(menuItemCategory.getTitle());
            categorySelected.setImageDrawable(ContextCompat.getDrawable(context, menuItemCategory.isSelected() ? R.drawable.ic_done_primary_24dp : R.drawable.ic_add_circle_primary_24dp));
        }
    }
}
