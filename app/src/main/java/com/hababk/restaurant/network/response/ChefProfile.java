package com.hababk.restaurant.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by a_man on 27-03-2018.
 */

public class ChefProfile implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("preorder")
    @Expose
    private Integer preorder;
    @SerializedName("serves_non_veg")
    @Expose
    private Integer serves_non_veg;
    @SerializedName("owner_id")
    @Expose
    private Integer owner_id;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("image_url")
    @Expose
    private String image_url;
    @SerializedName("delivery_time")
    @Expose
    private String delivery_time;
    @SerializedName("delivery_fee")
    @Expose
    private Double delivery_fee;
    @SerializedName("minimum_order")
    @Expose
    private Double minimum_order;
    @SerializedName("cost_for_two")
    @Expose
    private Double cost_for_two;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("opens_at")
    @Expose
    private String opens_at;
    @SerializedName("closes_at")
    @Expose
    private String closes_at;
    @SerializedName("categories")
    @Expose
    private ArrayList<MenuItemCategory> categories;

    protected ChefProfile(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            preorder = null;
        } else {
            preorder = in.readInt();
        }
        if (in.readByte() == 0) {
            serves_non_veg = null;
        } else {
            serves_non_veg = in.readInt();
        }
        if (in.readByte() == 0) {
            owner_id = null;
        } else {
            owner_id = in.readInt();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        name = in.readString();
        tagline = in.readString();
        image_url = in.readString();
        delivery_time = in.readString();
        if (in.readByte() == 0) {
            delivery_fee = null;
        } else {
            delivery_fee = in.readDouble();
        }
        if (in.readByte() == 0) {
            minimum_order = null;
        } else {
            minimum_order = in.readDouble();
        }
        if (in.readByte() == 0) {
            cost_for_two = null;
        } else {
            cost_for_two = in.readDouble();
        }
        details = in.readString();
        area = in.readString();
        address = in.readString();
        status = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        opens_at = in.readString();
        closes_at = in.readString();
        categories = in.createTypedArrayList(MenuItemCategory.CREATOR);
    }

    public static final Creator<ChefProfile> CREATOR = new Creator<ChefProfile>() {
        @Override
        public ChefProfile createFromParcel(Parcel in) {
            return new ChefProfile(in);
        }

        @Override
        public ChefProfile[] newArray(int size) {
            return new ChefProfile[size];
        }
    };

    public Double getMinimum_order() {
        return minimum_order;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPreorder() {
        return preorder != null ? preorder : 1;
    }

    public Integer getServes_non_veg() {
        return serves_non_veg!=null?serves_non_veg:1;
    }

    public Double getCost_for_two() {
        return cost_for_two;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public Double getLongitude() {
        return longitude != null ? longitude : 0.0d;
    }

    public Double getLatitude() {
        return latitude != null ? latitude : 0.0d;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getTagline() {
        return tagline != null ? tagline : "";
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDelivery_time() {
        return delivery_time != null ? delivery_time : "";
    }

    public Double getDelivery_fee() {
        return delivery_fee;
    }

    public String getDetails() {
        return details != null ? details : "";
    }

    public String getArea() {
        return area != null ? area : "";
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ArrayList<MenuItemCategory> getCategories() {
        return categories;
    }

    public String getOpens_at() {
        return opens_at;
    }

    public String getCloses_at() {
        return closes_at;
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
        if (preorder == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(preorder);
        }
        if (serves_non_veg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serves_non_veg);
        }
        if (owner_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(owner_id);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        dest.writeString(name);
        dest.writeString(tagline);
        dest.writeString(image_url);
        dest.writeString(delivery_time);
        if (delivery_fee == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(delivery_fee);
        }
        if (minimum_order == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minimum_order);
        }
        if (cost_for_two == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(cost_for_two);
        }
        dest.writeString(details);
        dest.writeString(area);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(opens_at);
        dest.writeString(closes_at);
        dest.writeTypedList(categories);
    }
}
