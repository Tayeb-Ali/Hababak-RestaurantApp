package com.hababk.appstore.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by a_man on 14-03-2018.
 */

public class MenuItem implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("is_available")
    @Expose
    private int is_available;

    @SerializedName("is_non_veg")
    @Expose
    private int is_non_veg;

    @SerializedName("store_id")
    @Expose
    private Integer store_id;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("specification")
    @Expose
    private String specification;

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("categories")
    @Expose
    private ArrayList<MenuItemCategory> categories;

    protected MenuItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        is_available = in.readInt();
        is_non_veg = in.readInt();
        if (in.readByte() == 0) {
            store_id = null;
        } else {
            store_id = in.readInt();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        title = in.readString();
        detail = in.readString();
        specification = in.readString();
        image_url = in.readString();
        status = in.readString();
        updated_at = in.readString();
        created_at = in.readString();
        categories = in.createTypedArrayList(MenuItemCategory.CREATOR);
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public ArrayList<MenuItemCategory> getCategories() {
        return categories;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public Integer getId() {
        return id;
    }

    public int getIs_available() {
        return is_available;
    }

    public int getIs_non_veg() {
        return is_non_veg;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public Double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getSpecification() {
        return specification;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getStatus() {
        return status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeInt(is_available);
        dest.writeInt(is_non_veg);
        if (store_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(store_id);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeString(title);
        dest.writeString(detail);
        dest.writeString(specification);
        dest.writeString(image_url);
        dest.writeString(status);
        dest.writeString(updated_at);
        dest.writeString(created_at);
        dest.writeTypedList(categories);
    }
}
