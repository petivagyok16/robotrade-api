package com.robotrade.robotradeapi.common;

import com.robotrade.robotradeapi.models.TraderBotTransaction;
import com.robotrade.robotradeapi.models.UserTransaction;

import java.util.UUID;

public class ShareCalculator {

	private static double calculateMargin(double currentUserValue, double latestAllUserValueHoldings) {
		return (currentUserValue / latestAllUserValueHoldings) * 100.00;
	}

	private static double calculateShare(double latestAllUserValueHoldings, double currentUserValue, double traderBotValueCapital) {
		return (traderBotValueCapital / 100.00) * ShareCalculator.calculateMargin(currentUserValue, latestAllUserValueHoldings);
	}

	public static UserTransaction calculateShare(double latestAllUserCashHoldings, double latestAllUserStockHoldings, double currentUserCash, double currentUserStock, TraderBotTransaction traderBotTransaction) {
		double cashShare = ShareCalculator.calculateShare(latestAllUserCashHoldings, currentUserCash, traderBotTransaction.getCash());
		double stockShare = ShareCalculator.calculateShare(latestAllUserStockHoldings, currentUserStock, traderBotTransaction.getStock());

		return new UserTransaction(UUID.randomUUID().toString(), traderBotTransaction.getType(), cashShare, stockShare, traderBotTransaction.getDate());
	}
}
