package com.robotrade.robotradeapi.rabbitMQ.dev.transactions;

import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class TransactionConfig {

	@Bean
	public TopicExchange roboExchange() {
		return new TopicExchange(TransactionConstants.ROBO_TRANSACTION_EXCHANGE_NAME);
	}

	private class TransactionReceiverConfig {

		@Bean
		public TransactionReceiver receiver() {
			return new TransactionReceiver();
		}

		@Bean
		public Queue roboTransactionQueue() {
			return new Queue(TransactionConstants.ROBO_TRANSACTION_QUEUE_NAME);
		}

		@Bean
		public Binding roboTransactionBinding(TopicExchange roboExchange, Queue roboTransactionQueue) {

			return BindingBuilder.bind(roboTransactionQueue)
							.to(roboExchange)
							.with(TransactionConstants.TRANSACTION_ROUTING_KEY);
		}
	}

}
