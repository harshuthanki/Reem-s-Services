package com.reemsservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("cat_img")
    @Expose
    private String catImg;
    @SerializedName("bus_id")
    @Expose
    private String busId;
    @SerializedName("bus_name")
    @Expose
    private String busName;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusUserId() {
        return busUserId;
    }

    public void setBusUserId(String busUserId) {
        this.busUserId = busUserId;
    }

    public String getTransCoin() {
        return transCoin;
    }

    public void setTransCoin(String transCoin) {
        this.transCoin = transCoin;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    @SerializedName("bus_user_id")
    @Expose
    private String busUserId;
    @SerializedName("trans_coin")
    @Expose
    private String transCoin;
    @SerializedName("trans_date")
    @Expose
    private String transDate;

}
