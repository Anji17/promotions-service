package com.verve.test.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Promotion {

	@Id
	private String id;
	private double price;
	private LocalDateTime expirationDate;

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

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Promotion(String id, double price, LocalDateTime expirationDate) {
        this.id = id;
        this.price = price;
        this.expirationDate = expirationDate;
    }
	
	public Promotion() {
		
	}
}
