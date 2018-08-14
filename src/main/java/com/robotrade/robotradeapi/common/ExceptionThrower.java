package com.robotrade.robotradeapi.common;

import com.robotrade.robotradeapi.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ExceptionThrower {

	public static void userNotFound(Throwable error) {
		log.error("User not found: " + error);
		throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
	}

	public static void emptySourceError(Throwable error) {
		throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessages.SOURCE_NOT_FOUND);
	}

}
