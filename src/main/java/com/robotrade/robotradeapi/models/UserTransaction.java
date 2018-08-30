package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class UserTransaction {

	private String id;
	private Number type;
	private double cash;
	private double stock;
	private Number date;

	public UserTransaction(String id, Number type, double cash, double stock, Number date) {
		this.id = id;
		this.type = type;
		this.cash = cash;
		this.stock = stock;
		this.date = date;
	}
}
