package com.reemsservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewBusinessModel {

	@SerializedName("cat_img")
	@Expose
	private String catImg;

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
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

	@SerializedName("coin_id")
	@Expose
	private String coinId;

	public String getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(String totalRating) {
		this.totalRating = totalRating;
	}

	@SerializedName("total_rating")
	@Expose
	private String totalRating;

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


	@SerializedName("bus_mobile")
	private String busMobile;


	@SerializedName("service_id")
	private String serviceId;

	@SerializedName("cat_id")
	private String catId;

	@SerializedName("bus_add")
	private String busAdd;


	@SerializedName("cat_name")
	private String catName;

	@SerializedName("service_name")
	private String serviceName;

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

	@SerializedName("service_price")
	private String servicePrice;

	@SerializedName("bus_img_one")
	private String busImgOne;

	@SerializedName("bus_img_two")
	private String busImgTwo;

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

	@SerializedName("bus_img_three")
	private String busImgThree;

	@SerializedName("schedule")
	private String schedule;

	public String getBusLoc() {
		return busLoc;
	}

	public void setBusLoc(String busLoc) {
		this.busLoc = busLoc;
	}

	@SerializedName("bus_loc")
	private String busLoc;



	public String getBusMobile() {
		return busMobile;
	}

	public void setBusMobile(String busMobile) {
		this.busMobile = busMobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getBusAdd() {
		return busAdd;
	}

	public void setBusAdd(String busAdd) {
		this.busAdd = busAdd;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCatImg() {
		return catImg;
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
}