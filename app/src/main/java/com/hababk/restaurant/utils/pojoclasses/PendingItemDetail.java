package com.hababk.appstore.utils.pojoclasses;

/**
 * Created by user on 1/31/2018.
 */

public class PendingItemDetail {


    private int mItemImageId=0;
    private String mItemName="";
    private String mItemType="";
    private int mItemStatusFlag=0;

    public PendingItemDetail(int mItemImageId, String mItemName, String mItemType, int mItemStatusFlag) {
        this.mItemImageId = mItemImageId;
        this.mItemName = mItemName;
        this.mItemType = mItemType;
        this.mItemStatusFlag=mItemStatusFlag;
    }

    public PendingItemDetail() {}

    public int getmItemImageId() {
        return mItemImageId;
    }

    public void setmItemImageId(int mItemImageId) {
        this.mItemImageId = mItemImageId;
    }

    public String getmItemName() {
        return mItemName;
    }

    public void setmItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public String getmItemType() {
        return mItemType;
    }

    public void setmItemType(String mItemType) {
        this.mItemType = mItemType;
    }

    public int getmItemStatusFlag() {
        return mItemStatusFlag;
    }

    public void setmItemStatusFlag(int mItemStatusFlag) {
        this.mItemStatusFlag = mItemStatusFlag;
    }

}
