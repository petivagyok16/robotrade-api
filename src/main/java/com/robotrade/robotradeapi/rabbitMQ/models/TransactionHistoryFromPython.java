package com.robotrade.robotradeapi.rabbitMQ.models;

import lombok.Data;

@Data
public class TransactionHistoryFromPython {
	Number type;
	Double capital;
	Double stocks;
	Number date; // epoch number

	public TransactionHistoryFromPython(
					Number type,
					Double capital,
					Double stocks,
					Long date) {
		this.type = type;
		this.capital = capital;
		this.stocks = stocks;
		this.date = date;
	}
}

