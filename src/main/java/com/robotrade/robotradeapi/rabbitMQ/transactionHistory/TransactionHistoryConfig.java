package com.robotrade.robotradeapi.rabbitMQ.transactionHistory;

import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionHistoryConstants;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionHistoryConfig {

	public static class ClientConfig {

		@Bean
		public DirectExchange exchange() {
			return new DirectExchange(TransactionHistoryConstants.TRANSACTION_HISTORY_EXCHANGE_NAME);
		}

		@Bean
		public TransactionHistoryClient client() {
			return new TransactionHistoryClient();
		}
	}
}
