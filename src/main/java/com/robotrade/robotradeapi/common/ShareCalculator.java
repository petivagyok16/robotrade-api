package com.robotrade.robotradeapi.common;

public class ShareCalculator {

	public static double calculateMargin(double capital, double userValue) {
		return (userValue / capital) * 100.00;
	}

	public static double calculateShare(double traderBotValueCapital, double margin) {
		return (traderBotValueCapital / 100.00) * margin;
	}
}
