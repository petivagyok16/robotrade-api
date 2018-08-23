package com.robotrade.robotradeapi.rabbitMQ.usersCapital;

import com.robotrade.robotradeapi.rabbitMQ.constants.UserCapitalConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersCapitalConfig {
	public static class ServerConfig {

		@Bean
		public Queue queue() {
			return new Queue(UserCapitalConstants.USER_CAPITAL_QUEUE_NAME);
		}

		@Bean
		public DirectExchange exchange() {
			return new DirectExchange(UserCapitalConstants.USER_CAPITAL_EXCHANGE_NAME);
		}

		@Bean
		public Binding binding(DirectExchange exchange,
													 Queue queue) {
			return BindingBuilder.bind(queue)
							.to(exchange)
							.with(UserCapitalConstants.USER_CAPITAL_REQUEST_ROUTING_KEY);
		}

		@Bean
		public UsersCapitalServer server() {
			return new UsersCapitalServer();
		}
	}
}
