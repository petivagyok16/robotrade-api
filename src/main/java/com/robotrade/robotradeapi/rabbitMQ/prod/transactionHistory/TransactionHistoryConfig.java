package com.robotrade.robotradeapi.rabbitMQ.prod.transactionHistory;

import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionHistoryConstants;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class TransactionHistoryConfig {

	@Autowired
	private final RabbitAdmin cloudAMQPAdmin;

	public TransactionHistoryConfig(RabbitAdmin cloudAMQPAdmin) {
		this.cloudAMQPAdmin = cloudAMQPAdmin;
	}

	public class TransactionHistoryClientConfig {

		@Bean
		public DirectExchange transactionHistoryExchange() {
			DirectExchange transactionHistoryExchange = new DirectExchange(TransactionHistoryConstants.TRANSACTION_HISTORY_EXCHANGE_NAME);
			TransactionHistoryConfig.this.cloudAMQPAdmin.declareExchange(transactionHistoryExchange);
			return transactionHistoryExchange;
		}

		@Bean
		public TransactionHistoryClient client() {
			return new TransactionHistoryClient();
		}
	}
}
