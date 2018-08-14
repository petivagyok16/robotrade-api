package com.robotrade.robotradeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RobotradeApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RobotradeApiApplication.class);
		app.setWebApplicationType(WebApplicationType.REACTIVE);
		app.run(args);
	}
}
