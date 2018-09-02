package com.robotrade.robotradeapi.controller;

import com.robotrade.robotradeapi.models.HttpResponseWrapper;
import com.robotrade.robotradeapi.models.Portfolio;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.models.UserTransaction;
import com.robotrade.robotradeapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(path = "/api/v1/users", produces = { APPLICATION_JSON_UTF8_VALUE })
@CrossOrigin(origins = "*")
@Validated
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getAllUser() {
		return this.userService.getAllUser();
	}

	@GetMapping(value = "/{id}")
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getUserProfile(@NotNull @PathVariable("id") String id) {
		return this.userService.getUserProfile(id);
	}

	@GetMapping(value = "/portfolio/{id}")
	public Mono<ResponseEntity<HttpResponseWrapper<Portfolio>>> getUserPortfolio(@NotNull @PathVariable("id") String id) {
		return this.userService.getUserPortfolio(id);
	}

	@GetMapping(value = "/{id}/transaction-history")
	public Mono<ResponseEntity<HttpResponseWrapper<List<UserTransaction>>>> getUserTransactionHistory(@NotNull @PathVariable("id") String id) {
		return this.userService.getUserTransactionHistory(id);
	}
}
