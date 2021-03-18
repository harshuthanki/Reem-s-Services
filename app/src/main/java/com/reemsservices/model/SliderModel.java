package com.reemsservices.model;

import com.google.gson.annotations.SerializedName;

public class SliderModel {

	@SerializedName("date")
	private String date;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("slider_satus")
	private String sliderSatus;

	@SerializedName("slider_photo")
	private String sliderPhoto;

	@SerializedName("slider_id")
	private String sliderId;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setSliderSatus(String sliderSatus){
		this.sliderSatus = sliderSatus;
	}

	public String getSliderSatus(){
		return sliderSatus;
	}

	public void setSliderPhoto(String sliderPhoto){
		this.sliderPhoto = sliderPhoto;
	}

	public String getSliderPhoto(){
		return sliderPhoto;
	}

	public void setSliderId(String sliderId){
		this.sliderId = sliderId;
	}

	public String getSliderId(){
		return sliderId;
	}
}