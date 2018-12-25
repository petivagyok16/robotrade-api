package com.robotrade.robotradeapi.rabbitMQ.prod.transactions;

import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class TransactionConfig {

	@Autowired
	private final RabbitAdmin cloudAMQPAdmin;

	public TransactionConfig(RabbitAdmin cloudAMQPAdmin) {
		this.cloudAMQPAdmin = cloudAMQPAdmin;
	}

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
			Queue roboTransactionQueue = new Queue(TransactionConstants.ROBO_TRANSACTION_QUEUE_NAME);
			TransactionConfig.this.cloudAMQPAdmin.declareQueue(roboTransactionQueue);
			return roboTransactionQueue;
		}

		@Bean
		public Binding roboTransactionBinding(TopicExchange roboExchange, Queue roboTransactionQueue) {

			Binding roboTransactionBinding = BindingBuilder.bind(roboTransactionQueue)
							.to(roboExchange)
							.with(TransactionConstants.TRANSACTION_ROUTING_KEY);

			TransactionConfig.this.cloudAMQPAdmin.declareBinding(roboTransactionBinding);
			return roboTransactionBinding;
		}
	}

}