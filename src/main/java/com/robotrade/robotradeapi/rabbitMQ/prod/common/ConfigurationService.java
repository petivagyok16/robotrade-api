package com.robotrade.robotradeapi.rabbitMQ.prod.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	@Value("${CLOUDAMQP_HOSTNAME}")
	private String hostName;

	@Value("${CLOUDAMQP_PASSWORD}")
	private String password;

	@Value("${CLOUDAMQP_USERNAME}")
	private String username;

	@Value("${CLOUDAMQP_VIRTUALHOST}")
	private String virtualhost;

	public String getHostName() {
		return hostName;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getVirtualhost() {
		return virtualhost;
	}
}

