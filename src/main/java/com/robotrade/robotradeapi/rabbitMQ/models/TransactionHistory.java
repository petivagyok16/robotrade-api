package com.robotrade.robotradeapi.rabbitMQ.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionHistory {
	Number type;
	Double capital;
	Double stocks;
	Number date; // epoch number

	public TransactionHistory(
					@JsonProperty("type") Number type,
					@JsonProperty("capital") Double capital,
					@JsonProperty("stocks") Double stocks,
					@JsonProperty("date") Long date) {
		this.type = type;
		this.capital = capital;
		this.stocks = stocks;
		this.date = date;
	}
}

