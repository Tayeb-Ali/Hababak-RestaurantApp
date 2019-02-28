package com.hababk.appstore.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarningResponse {
    @SerializedName("total_earnings")
    @Expose
    private Double total_earnings;
    @SerializedName("last_earning_date")
    @Expose
    private String last_earning_date;
    @SerializedName("earnings")
    @Expose
    private BaseListModel<Earning> earnings;

    public Double getTotal_earnings() {
        return total_earnings;
    }

    public BaseListModel<Earning> getEarnings() {
        return earnings;
    }

    public String getLast_earning_date() {
        return last_earning_date;
    }
}
