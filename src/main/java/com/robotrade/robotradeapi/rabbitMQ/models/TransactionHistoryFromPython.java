package com.robotrade.robotradeapi.rabbitMQ.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionHistoryFromPython {
	Number type;
	Double capital;
	Double stocks;
	Number date; // epoch number

	public TransactionHistoryFromPython(
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

