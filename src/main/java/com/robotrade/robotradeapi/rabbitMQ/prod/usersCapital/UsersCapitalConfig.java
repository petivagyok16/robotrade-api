package com.robotrade.robotradeapi.rabbitMQ.prod.usersCapital;

import com.robotrade.robotradeapi.rabbitMQ.constants.UserCapitalConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class UsersCapitalConfig {

	@Autowired
	private final RabbitAdmin cloudAMQPAdmin;

	public UsersCapitalConfig(RabbitAdmin cloudAMQPAdmin) {
		this.cloudAMQPAdmin = cloudAMQPAdmin;
	}

	public class UsersCapitalServerConfig {

		@Bean
		public Queue usersCapitalQueue() {
			Queue usersCapitalQueue = new Queue(UserCapitalConstants.USERS_CAPITAL_QUEUE_NAME);
			UsersCapitalConfig.this.cloudAMQPAdmin.declareQueue(usersCapitalQueue);
			return usersCapitalQueue;
		}

		@Bean
		public DirectExchange usersCapitalExchange() {
			DirectExchange usersCapitalExchange = new DirectExchange(UserCapitalConstants.USERS_CAPITAL_EXCHANGE_NAME);
			UsersCapitalConfig.this.cloudAMQPAdmin.declareExchange(usersCapitalExchange);
			return usersCapitalExchange;
		}

		@Bean
		public Binding usersCapitalBinding(DirectExchange usersCapitalExchange, Queue usersCapitalQueue) {
			Binding usersCapitalBinding = BindingBuilder.bind(usersCapitalQueue)
							.to(usersCapitalExchange)
							.with(UserCapitalConstants.USERS_CAPITAL_REQUEST_ROUTING_KEY);
			UsersCapitalConfig.this.cloudAMQPAdmin.declareBinding(usersCapitalBinding);
			return usersCapitalBinding;
		}

		@Bean
		public UsersCapitalServer usersCapitalServer() {
			return new UsersCapitalServer();
		}
	}
}
