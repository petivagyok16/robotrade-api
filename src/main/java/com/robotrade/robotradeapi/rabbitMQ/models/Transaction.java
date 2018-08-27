package com.robotrade.robotradeapi.rabbitMQ.models;

import lombok.Data;

@Data
public class Transaction {

	private String id;
	private Number type;
	private Double cash;
	private Double stock;
	private Number date;

	public Transaction(Number type, Double cash, Double stock, Number date) {
		this.type = type;
		this.cash = cash;
		this.stock = stock;
		this.date = date;
	}
}