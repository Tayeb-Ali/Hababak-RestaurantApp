package com.hababk.restaurant.utils.pojoclasses;

/**
 * Created by user on 2/3/2018.
 */

public class TodayEarningDetail {

    private String mServicePrice="";
    private String mDeliveryCharges="";
    private String mAdminProfit="";
    private String mDeliverymanProfit="";
    private String mPaidOrderAmt="";
    private String mCashAmt="";
    private String mCashOnHand="";

    public TodayEarningDetail(String mServicePrice, String mDeliveryCharges, String mAdminProfit, String mDeliverymanProfit, String mPaidOrderAmt, String mCashAmt, String mCashOnHand) {
        this.mServicePrice = mServicePrice;
        this.mDeliveryCharges = mDeliveryCharges;
        this.mAdminProfit = mAdminProfit;
        this.mDeliverymanProfit = mDeliverymanProfit;
        this.mPaidOrderAmt = mPaidOrderAmt;
        this.mCashAmt = mCashAmt;
        this.mCashOnHand = mCashOnHand;
    }

    public TodayEarningDetail() {}

    public String getmServicePrice() {
        return mServicePrice;
    }

    public void setmServicePrice(String mServicePrice) {
        this.mServicePrice = mServicePrice;
    }

    public String getmDeliveryCharges() {
        return mDeliveryCharges;
    }

    public void setmDeliveryCharges(String mDeliveryCharges) {
        this.mDeliveryCharges = mDeliveryCharges;
    }

    public String getmAdminProfit() {
        return mAdminProfit;
    }

    public void setmAdminProfit(String mAdminProfit) {
        this.mAdminProfit = mAdminProfit;
    }

    public String getmDeliverymanProfit() {
        return mDeliverymanProfit;
    }

    public void setmDeliverymanProfit(String mDeliverymanProfit) {
        this.mDeliverymanProfit = mDeliverymanProfit;
    }

    public String getmPaidOrderAmt() {
        return mPaidOrderAmt;
    }

    public void setmPaidOrderAmt(String mPaidOrderAmt) {
        this.mPaidOrderAmt = mPaidOrderAmt;
    }

    public String getmCashAmt() {
        return mCashAmt;
    }

    public void setmCashAmt(String mCashAmt) {
        this.mCashAmt = mCashAmt;
    }

    public String getmCashOnHand() {
        return mCashOnHand;
    }

    public void setmCashOnHand(String mCashOnHand) {
        this.mCashOnHand = mCashOnHand;
    }
}
