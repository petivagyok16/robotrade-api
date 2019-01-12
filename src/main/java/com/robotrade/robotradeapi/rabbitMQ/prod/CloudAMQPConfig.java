package com.robotrade.robotradeapi.rabbitMQ.prod;

import com.robotrade.robotradeapi.rabbitMQ.prod.common.ConfigurationService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableRabbit
@Profile("prod")
public class CloudAMQPConfig {

	private CachingConnectionFactory cloudAMPQConnection;

	@Autowired
	private ConfigurationService configurationService;

	public CloudAMQPConfig(ConfigurationService configurationService) {
		this.configurationService = configurationService;
		this.setupConnection();
	}

	private void setupConnection() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.configurationService.getHostName());
		connectionFactory.setUsername(this.configurationService.getUsername());
		connectionFactory.setPassword(this.configurationService.getPassword());
		connectionFactory.setVirtualHost(this.configurationService.getVirtualhost());

		// Recommended settings
		connectionFactory.setRequestedHeartBeat(30);
		connectionFactory.setConnectionTimeout(30000);

		this.cloudAMPQConnection = connectionFactory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConcurrentConsumers(2);
		factory.setConnectionFactory(this.cloudAMPQConnection);
		return factory;
	}

	@Bean
	public RabbitAdmin cloudAMQPAdmin() {
		return new RabbitAdmin(this.cloudAMPQConnection);
	}

	@Bean
	public RabbitTemplate cloudRabbitTemplate() {
		return new RabbitTemplate(this.cloudAMPQConnection);
	}
}
