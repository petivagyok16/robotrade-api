package com.robotrade.robotradeapi.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
	public TopicExchange roboExchange() {
		return new TopicExchange(RabbitConstants.ROBO_TOPIC_EXCHANGE_NAME);
	}

	private static class TransactionReceiverConfig {

		@Bean
		public TransactionReceiver receiver() {
			return new TransactionReceiver();
		}

		@Bean
		public Queue roboTransactionQueue() {
			return new Queue(RabbitConstants.ROBO_TRANSACTION_QUEUE_NAME);
		}

		@Bean
		public Binding roboTransactionBinding(TopicExchange roboExchange,
																					Queue roboTransactionQueue) {
			return BindingBuilder.bind(roboTransactionQueue)
							.to(roboExchange)
							.with(RabbitConstants.TRANSACTION_ROUTING_KEY);
		}

		@Bean
		public Binding roboTransactionHistoryBinding(TopicExchange roboExchange,
																								 Queue roboTransactionQueue) {
			return BindingBuilder.bind(roboTransactionQueue)
							.to(roboExchange)
							.with(RabbitConstants.TRANSACTION_HISTORY_ROUTING_KEY);
		}
	}

}