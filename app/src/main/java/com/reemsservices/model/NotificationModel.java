package com.reemsservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("notify_id")
    @Expose
    private String notifyId;
    @SerializedName("from_id")
    @Expose
    private String fromId;
    @SerializedName("any_id")
    @Expose
    private String anyId;
    @SerializedName("to_id")
    @Expose
    private String toId;
    @SerializedName("notify_title")
    @Expose
    private String notifyTitle;
    @SerializedName("to_notify_desc")
    @Expose
    private String toNotifyDesc;
    @SerializedName("from_notify_desc")
    @Expose
    private String fromNotifyDesc;
    @SerializedName("notify_type")
    @Expose
    private String notifyType;
    @SerializedName("notify_status")
    @Expose
    private String notifyStatus;

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }

    @SerializedName("notification_count")
    @Expose
    private int notification_count;


    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getAnyId() {
        return anyId;
    }

    public void setAnyId(String anyId) {
        this.anyId = anyId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getToNotifyDesc() {
        return toNotifyDesc;
    }

    public void setToNotifyDesc(String toNotifyDesc) {
        this.toNotifyDesc = toNotifyDesc;
    }

    public String getFromNotifyDesc() {
        return fromNotifyDesc;
    }

    public void setFromNotifyDesc(String fromNotifyDesc) {
        this.fromNotifyDesc = fromNotifyDesc;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getNotifyCreateDate() {
        return notifyCreateDate;
    }

    public void setNotifyCreateDate(String notifyCreateDate) {
        this.notifyCreateDate = notifyCreateDate;
    }

    public Object getCatImg() {
        return catImg;
    }

    public void setCatImg(Object catImg) {
        this.catImg = catImg;
    }

    @SerializedName("notify_create_date")
    @Expose
    private String notifyCreateDate;
    @SerializedName("catImg")
    @Expose
    private Object catImg;

}
