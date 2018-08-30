package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class TraderBotTransaction {

	private Number type;
	private double cash;
	private double stock;
	private Number date;

	public TraderBotTransaction(Number type, double cash, double stock, Number date) {
		this.type = type;
		this.cash = cash;
		this.stock = stock;
		this.date = date;
	}
}