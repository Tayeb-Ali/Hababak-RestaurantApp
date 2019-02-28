package com.hababk.restaurant.utils.pojoclasses;

/**
 * Created by user on 2/2/2018.
 */

public class ReviewDetail {

    private String mPersonName="";
    private String mPersonAddress="";
    private double mRating;
    private String mReview="";
    private String mDate="";

    public ReviewDetail(String mPersonName, String mPersonAddress, double mRating, String mReview, String mDate) {
        this.mPersonName = mPersonName;
        this.mPersonAddress = mPersonAddress;
        this.mRating = mRating;
        this.mReview = mReview;
        this.mDate = mDate;
    }

    public String getmPersonName() {
        return mPersonName;
    }

    public void setmPersonName(String mPersonName) {
        this.mPersonName = mPersonName;
    }

    public String getmPersonAddress() {
        return mPersonAddress;
    }

    public void setmPersonAddress(String mPersonAddress) {
        this.mPersonAddress = mPersonAddress;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
