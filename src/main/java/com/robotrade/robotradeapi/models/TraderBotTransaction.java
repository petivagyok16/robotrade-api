package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class TraderBotTransaction {

	private Number type;
	private Double cash;
	private Double stock;
	private Number date;

	public TraderBotTransaction(Number type, Double cash, Double stock, Number date) {
		this.type = type;
		this.cash = cash;
		this.stock = stock;
		this.date = date;
	}
}