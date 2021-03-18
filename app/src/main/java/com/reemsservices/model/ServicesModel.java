package com.reemsservices.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesModel {

	@SerializedName("cat_status")
	private String catStatus;

	@SerializedName("cat_img")
	private String catImg;

	@SerializedName("cat_parent_id")
	private String catParentId;

	@SerializedName("cat_name")
	private String catName;

	@SerializedName("cat_id")
	private String catId;

	@SerializedName("created_date")
	private String createdDate;

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

	@SerializedName("cat_sub")
	private List<SubServicesModel> catSub;




	public void setCatStatus(String catStatus){
		this.catStatus = catStatus;
	}

	public String getCatStatus(){
		return catStatus;
	}

	public void setCatImg(String catImg){
		this.catImg = catImg;
	}

	public String getCatImg(){
		return catImg;
	}

	public void setCatParentId(String catParentId){
		this.catParentId = catParentId;
	}

	public String getCatParentId(){
		return catParentId;
	}

	public void setCatName(String catName){
		this.catName = catName;
	}

	public String getCatName(){
		return catName;
	}

	public void setCatId(String catId){
		this.catId = catId;
	}

	public String getCatId(){
		return catId;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setCatSub(List<SubServicesModel> catSub){
		this.catSub = catSub;
	}

	public List<SubServicesModel> getCatSub(){
		return catSub;
	}
}