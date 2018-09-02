package com.robotrade.robotradeapi.models;

import lombok.Data;

@Data
public class LoggedInUser {
	private User user;
	private String token;

	public LoggedInUser(User user, String token) {
		this.user = user;
		this.token = token;
	}
}
