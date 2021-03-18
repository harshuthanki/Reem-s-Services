package com.reemsservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBusinessModel {

    @SerializedName("rating_id")
    @Expose
    private String ratingId;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("bus_review")
    @Expose
    private String busReview;

    @SerializedName("cat_img")
    @Expose
    private String catImg;


    @SerializedName("services")
    @Expose
    List<GetBusinessModel> list_service = null;

    @SerializedName("review")
    @Expose
    List<GetBusinessModel> list_review = null;

    @SerializedName("service_id")
    @Expose
    private String serviceId;

    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_price")
    @Expose
    private String servicePrice;

    private String status;
    @SerializedName("service_create_date")
    @Expose
    private String serviceCreateDate;


    @SerializedName("user_profile")
    @Expose
    private String userProfile;

    @SerializedName("bus_id")
    @Expose
    private String busId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("bus_name")
    @Expose
    private String busName;
    @SerializedName("bus_add")
    @Expose
    private String busAdd;
    @SerializedName("bus_mobile")
    @Expose
    private String busMobile;
    @SerializedName("total_rating")
    @Expose
    private String totalRating;
    @SerializedName("bus_img_one")
    @Expose
    private String busImgOne;
    @SerializedName("bus_img_two")
    @Expose
    private String busImgTwo;
    @SerializedName("bus_img_three")
    @Expose
    private String busImgThree;
    @SerializedName("schedule")
    @Expose
    private String schedule;
    @SerializedName("bus_loc")
    @Expose
    private String busLoc;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    @SerializedName("cat_name")
    @Expose
    private String cat_name;

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    @SerializedName("business")
    @Expose
    private List<GetBusinessModel> business_list;

    public List<GetBusinessModel> getBusiness_list() {
        return business_list;
    }

    public void setBusiness_list(List<GetBusinessModel> business_list) {
        this.business_list = business_list;
    }

    public boolean isSelected = false;

    public List<GetBusinessModel> getList_service() {
        return list_service;
    }


    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusAdd() {
        return busAdd;
    }

    public void setBusAdd(String busAdd) {
        this.busAdd = busAdd;
    }

    public String getBusMobile() {
        return busMobile;
    }

    public void setBusMobile(String busMobile) {
        this.busMobile = busMobile;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getBusImgOne() {
        return busImgOne;
    }

    public void setBusImgOne(String busImgOne) {
        this.busImgOne = busImgOne;
    }

    public String getBusImgTwo() {
        return busImgTwo;
    }

    public void setBusImgTwo(String busImgTwo) {
        this.busImgTwo = busImgTwo;
    }

    public String getBusImgThree() {
        return busImgThree;
    }

    public void setBusImgThree(String busImgThree) {
        this.busImgThree = busImgThree;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getBusLoc() {
        return busLoc;
    }

    public void setBusLoc(String busLoc) {
        this.busLoc = busLoc;
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

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }


    public void setList_service(List<GetBusinessModel> list_service) {
        this.list_service = list_service;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceCreateDate() {
        return serviceCreateDate;
    }

    public void setServiceCreateDate(String serviceCreateDate) {
        this.serviceCreateDate = serviceCreateDate;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBusReview() {
        return busReview;
    }

    public void setBusReview(String busReview) {
        this.busReview = busReview;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<GetBusinessModel> getList_review() {
        return list_review;
    }

    public void setList_review(List<GetBusinessModel> list_review) {
        this.list_review = list_review;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
