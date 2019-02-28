package com.hababk.appstore.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Earning {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer order_id;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("paid")
    @Expose
    private int paid;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("order")
    @Expose
    private Order order;

    private String tag;

    public Earning(String s) {
        this.tag = s;
    }

    public String getTag() {
        return tag;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public int getPaid() {
        return paid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Order getOrder() {
        return order;
    }
}
