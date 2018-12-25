package com.robotrade.robotradeapi.rabbitMQ.dev.usersCapital;

import com.robotrade.robotradeapi.rabbitMQ.constants.UserCapitalConstants;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@NoArgsConstructor
@Profile("dev")
public class UsersCapitalConfig {

	public class UsersCapitalServerConfig {

		@Bean
		public Queue usersCapitalQueue() {
			return new Queue(UserCapitalConstants.USERS_CAPITAL_QUEUE_NAME);
		}

		@Bean
		public DirectExchange usersCapitalExchange() {
			return new DirectExchange(UserCapitalConstants.USERS_CAPITAL_EXCHANGE_NAME);
		}

		@Bean
		public Binding usersCapitalBinding(DirectExchange usersCapitalExchange, Queue usersCapitalQueue) {
			return BindingBuilder.bind(usersCapitalQueue)
							.to(usersCapitalExchange)
							.with(UserCapitalConstants.USERS_CAPITAL_REQUEST_ROUTING_KEY);
		}

		@Bean
		public UsersCapitalServer usersCapitalServer() {
			return new UsersCapitalServer();
		}
	}
}

