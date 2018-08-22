package com.robotrade.robotradeapi.rabbitMQ.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionHistory {
	String type;
	Double capital;
	Double stock;
	Number date; // epoch number

	public TransactionHistory(
					@JsonProperty("type") String type,
					@JsonProperty("capital") Double capital,
					@JsonProperty("stocks") Double stocks,
					@JsonProperty("date") Number date) {
		this.type = type;
		this.capital = capital;
		this.stock = stock;
		this.date = date;
	}
}

