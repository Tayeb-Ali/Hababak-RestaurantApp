package com.hababk.restaurant.utils.pojoclasses;

/**
 * Created by user on 2/5/2018.
 */

public class MarkerData {

    private double mLatitude;
    private double mLongitude;
    private String mTitle="";
    private String mSnippet="";
    private int mResId;

    public MarkerData(double mLatitude, double mLongitude, String mTitle, String mSnippet, int mResId) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.mResId = mResId;
    }

    public MarkerData() {}

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }

    public int getmResId() {
        return mResId;
    }

    public void setmResId(int mResId) {
        this.mResId = mResId;
    }
}
