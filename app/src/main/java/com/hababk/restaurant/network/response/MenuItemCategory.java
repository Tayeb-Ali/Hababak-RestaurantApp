package com.hababk.restaurant.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tayeb-Ali on 15-03-2018.
 */

public class MenuItemCategory implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stores_count")
    @Expose
    private Integer stores_count;
    @SerializedName("image_url")
    @Expose
    private String image_url;
    @SerializedName("title")
    @Expose
    private String title;

    private boolean selected;

    public Integer getId() {
        return id;
    }

    public Integer getStores_count() {
        return stores_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return selected;
    }

    public MenuItemCategory() {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MenuItemCategory(Integer id) {
        this.id = id;
    }

    public MenuItemCategory(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    protected MenuItemCategory(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            stores_count = null;
        } else {
            stores_count = in.readInt();
        }
        image_url = in.readString();
        title = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<MenuItemCategory> CREATOR = new Creator<MenuItemCategory>() {
        @Override
        public MenuItemCategory createFromParcel(Parcel in) {
            return new MenuItemCategory(in);
        }

        @Override
        public MenuItemCategory[] newArray(int size) {
            return new MenuItemCategory[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItemCategory)) return false;

        MenuItemCategory category = (MenuItemCategory) o;

        return getId().equals(category.getId());
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
        if (stores_count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stores_count);
        }
        dest.writeString(image_url);
        dest.writeString(title);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
