package com.robotrade.robotradeapi.rabbitMQ.config;

import com.robotrade.robotradeapi.rabbitMQ.constants.CloudAMPQCredentials;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudAMQPConfig {

	private final CachingConnectionFactory cloudAMPQConnection;

	public CloudAMQPConfig() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(CloudAMPQCredentials.HOSTNAME);
		connectionFactory.setUsername(CloudAMPQCredentials.USERNAME);
		connectionFactory.setPassword(CloudAMPQCredentials.PASSWORD);
		connectionFactory.setVirtualHost(CloudAMPQCredentials.VIRTUAL_HOST);

		// Recommended settings
		connectionFactory.setRequestedHeartBeat(30);
		connectionFactory.setConnectionTimeout(30000);

		this.cloudAMPQConnection = connectionFactory;
	}

	@Bean
	public RabbitAdmin cloudAMPQAdmin() {
		return new RabbitAdmin(this.cloudAMPQConnection);
	}

	@Bean
	public RabbitTemplate cloudRabbitTemplate() {
		return new RabbitTemplate(this.cloudAMPQConnection);
	}
}
