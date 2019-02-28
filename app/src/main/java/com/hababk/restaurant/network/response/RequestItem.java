package com.hababk.appstore.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by a_man on 27-03-2018.
 */

public class RequestItem implements Parcelable{
    @SerializedName("order_id")
    @Expose
    private Integer order_id;
    @SerializedName("menu_item_id")
    @Expose
    private Integer menu_item_id;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("menuitem")
    @Expose
    private MenuItem menuitem;



    protected RequestItem(Parcel in) {
        if (in.readByte() == 0) {
            order_id = null;
        } else {
            order_id = in.readInt();
        }
        if (in.readByte() == 0) {
            menu_item_id = null;
        } else {
            menu_item_id = in.readInt();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readDouble();
        }
        menuitem = in.readParcelable(MenuItem.class.getClassLoader());
    }

    public static final Creator<RequestItem> CREATOR = new Creator<RequestItem>() {
        @Override
        public RequestItem createFromParcel(Parcel in) {
            return new RequestItem(in);
        }

        @Override
        public RequestItem[] newArray(int size) {
            return new RequestItem[size];
        }
    };

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getMenu_item_id() {
        return menu_item_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotal() {
        return total;
    }

    public MenuItem getMenuitem() {
        return menuitem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (order_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order_id);
        }
        if (menu_item_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(menu_item_id);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(total);
        }
        dest.writeParcelable(menuitem, flags);
    }
}
