package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class Portfolio {
	public String username;
	public Double cash;
	public Double stock;
	public Double portfolio;

	public Portfolio(String username, Double cash, Double stock, Double portfolio) {
		this.username = username;
		this.cash = cash;
		this.stock = stock;
		this.portfolio = portfolio;
	}
}
