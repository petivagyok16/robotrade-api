package com.robotrade.robotradeapi.rabbitMQ.models;

import lombok.Data;

@Data
public class Transaction {
	private Number type;
	private Double cash;
	private Double stock;

	public Transaction(Number type, Double cash, Double stock) {
		this.type = type;
		this.cash = cash;
		this.stock = stock;
	}
}