package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class UserTransaction {

	private String id;
	private Number type;
	private Double cash;
	private Double stock;
	private Number date;

	public UserTransaction(String id, Number type, Double cash, Double stock, Number date) {
		this.id = id;
		this.type = type;
		this.cash = cash;
		this.stock = stock;
		this.date = date;
	}
}
