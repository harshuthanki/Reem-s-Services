package com.reemsservices.model;

import android.app.Service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewBusinessModel {

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
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("created_date")
	@Expose
	private String createdDate;
	@SerializedName("cat_img")
	@Expose
	private String catImg;
	@SerializedName("cat_name")
	@Expose
	private String catName;
	@SerializedName("services")
	@Expose
	private List<ViewBusinessModel> list_services = null;

	@SerializedName("city_id")
	@Expose
	private String cityId;
	@SerializedName("city_name")
	@Expose
	private String cityName;
	@SerializedName("city_createDate")
	@Expose
	private String cityCreateDate;

	@SerializedName("service_id")
	@Expose
	private String serviceId;

	@SerializedName("service_name")
	@Expose
	private String serviceName;
	@SerializedName("service_price")
	@Expose
	private String servicePrice;

	@SerializedName("service_create_date")
	@Expose
	private String serviceCreateDate;

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

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public List<ViewBusinessModel> getServices() {
		return list_services;
	}

	public void setServices(List<ViewBusinessModel> services) {
		this.list_services = services;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCreateDate() {
		return cityCreateDate;
	}

	public void setCityCreateDate(String cityCreateDate) {
		this.cityCreateDate = cityCreateDate;
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
}