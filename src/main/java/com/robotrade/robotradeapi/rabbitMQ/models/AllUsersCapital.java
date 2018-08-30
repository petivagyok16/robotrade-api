package com.robotrade.robotradeapi.rabbitMQ.models;

import lombok.Data;

@Data
public class AllUsersCapital {
	private double capital;

	public AllUsersCapital(double capital) {
		this.capital = capital;
	}
}
