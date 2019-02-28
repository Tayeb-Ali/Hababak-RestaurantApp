package com.hababk.appstore.utils.pojoclasses;

/**
 * Created by user on 1/31/2018.
 */

public class ApprovedItemDetail {

    private int mItemImageId=0;
    private String mItemName="";
    private String mItemType="";
    private String mPrice="";
    private boolean mAvailableStatus=false;

    public ApprovedItemDetail(int mItemImageId, String mItemName, String mItemType, String mPrice, boolean mAvailableStatus) {
        this.mItemImageId = mItemImageId;
        this.mItemName = mItemName;
        this.mItemType = mItemType;
        this.mPrice = mPrice;
        this.mAvailableStatus = mAvailableStatus;
    }

    public ApprovedItemDetail() {}

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

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public boolean ismAvailableStatus() {
        return mAvailableStatus;
    }

    public void setmAvailableStatus(boolean mAvailableStatus) {
        this.mAvailableStatus = mAvailableStatus;
    }
}
