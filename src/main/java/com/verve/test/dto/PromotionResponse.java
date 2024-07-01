package com.verve.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PromotionResponse {

	String id;
	double price;
	
	@JsonProperty("expiration_date")
	String expirationDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	@Override
	public String toString() {
		return "Promotion [id=" + id + ", price=" + price + ", expirationDate=" + expirationDate + "]";
	}
	public PromotionResponse(String id, double price, String expirationDate) {
		super();
		this.id = id;
		this.price = price;
		this.expirationDate = expirationDate;
	}	
	
	
}
