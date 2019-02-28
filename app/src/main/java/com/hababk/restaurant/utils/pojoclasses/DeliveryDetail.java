package com.hababk.appstore.utils.pojoclasses;

/**
 * Created by user on 1/30/2018.
 */

public class DeliveryDetail {

    private String mPersonName="";
    private String mPaymentMethod="";
    private String mOrderNumber="";
    private String mDispatchDate="";
    private int flag=0;

    public DeliveryDetail(String mPersonName, String mPaymentMethod, String mOrderNumber, String mDispatchDate, int flag) {
        this.mPersonName = mPersonName;
        this.mPaymentMethod = mPaymentMethod;
        this.mOrderNumber = mOrderNumber;
        this.mDispatchDate = mDispatchDate;
        this.flag = flag;
    }

    public DeliveryDetail() {}

    public int getFlag() {
        return flag;
    }

    public String getmDispatchDate() {
        return mDispatchDate;
    }

    public String getmOrderNumber() {
        return mOrderNumber;
    }

    public String getmPaymentMethod() {
        return mPaymentMethod;
    }

    public String getmPersonName() {
        return mPersonName;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setmDispatchDate(String mDispatchDate) {
        this.mDispatchDate = mDispatchDate;
    }

    public void setmOrderNumber(String mOrderNumber) {
        this.mOrderNumber = mOrderNumber;
    }

    public void setmPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public void setmPersonName(String mPersonName) {
        this.mPersonName = mPersonName;
    }

}
