package com.hababk.restaurant.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tayeb-Ali on 14-03-2018.
 */

public class BaseListModel<T> {
    @SerializedName("current_page")
    @Expose
    private int current_page;

    @SerializedName("last_page")
    @Expose
    private int last_page;

    @SerializedName("per_page")
    @Expose
    private int per_page;

    @SerializedName("data")
    @Expose
    private ArrayList<T> data;

    public int getCurrent_page() {
        return current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public ArrayList<T> getData() {
        return data;
    }
}
