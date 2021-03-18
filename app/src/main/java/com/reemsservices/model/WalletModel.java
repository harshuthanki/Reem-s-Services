package com.reemsservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletModel {


    @SerializedName("cat_img")
    @Expose
    private String catImg;
    @SerializedName("coin_id")
    @Expose
    private String coinId;

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCodeGenerate() {
        return codeGenerate;
    }

    public void setCodeGenerate(String codeGenerate) {
        this.codeGenerate = codeGenerate;
    }

    public String getUsedUserId() {
        return usedUserId;
    }

    public void setUsedUserId(String usedUserId) {
        this.usedUserId = usedUserId;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("bus_id")
    @Expose
    private String busId;
    @SerializedName("code_generate")
    @Expose
    private String codeGenerate;
    @SerializedName("used_user_id")
    @Expose
    private String usedUserId;
    @SerializedName("coin")
    @Expose
    private String coin;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("bus_name")
    @Expose
    private String busName;


    @SerializedName("user_coin")
    @Expose
    private String userCoin;
    @SerializedName("user_ref_code")
    @Expose
    private String userRefCode;
    @SerializedName("coin_created")
    @Expose
    private List<Object> coinCreated = null;
    @SerializedName("business_view")
    @Expose
    private List<ViewBusinessModel> businessView = null;
    @SerializedName("transaction_array")
    @Expose
    private List<TransactionModel> transactionArray = null;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;

    @SerializedName("bus_user_id")
    @Expose
    private String busUserId;

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

    public String getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(String userCoin) {
        this.userCoin = userCoin;
    }

    public String getUserRefCode() {
        return userRefCode;
    }

    public void setUserRefCode(String userRefCode) {
        this.userRefCode = userRefCode;
    }

    public List<Object> getCoinCreated() {
        return coinCreated;
    }

    public void setCoinCreated(List<Object> coinCreated) {
        this.coinCreated = coinCreated;
    }

    public List<ViewBusinessModel> getBusinessView() {
        return businessView;
    }

    public void setBusinessView(List<ViewBusinessModel> businessView) {
        this.businessView = businessView;
    }

    public List<TransactionModel> getTransactionArray() {
        return transactionArray;
    }

    public void setTransactionArray(List<TransactionModel> transactionArray) {
        this.transactionArray = transactionArray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    @SerializedName("trans_coin")
    @Expose
    private String transCoin;
    @SerializedName("trans_date")
    @Expose
    private String transDate;



}
