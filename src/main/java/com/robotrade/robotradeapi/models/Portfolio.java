package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class Portfolio {
	public String username;
	public double cash;
	public double stock;
	public double portfolio;

	public Portfolio() {}

	public Portfolio(String username, double cash, double stock, double portfolio) {
		this.username = username;
		this.cash = cash;
		this.stock = stock;
		this.portfolio = portfolio;
	}
}
