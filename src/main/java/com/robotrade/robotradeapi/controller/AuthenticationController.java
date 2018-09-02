package com.robotrade.robotradeapi.controller;

import com.robotrade.robotradeapi.models.HttpResponseWrapper;
import com.robotrade.robotradeapi.models.LoggedInUser;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.security.JwtAuthenticationRequest;
import com.robotrade.robotradeapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private final AuthenticationService authService;

	@Autowired
	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping(
					value = "/signin",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<HttpResponseWrapper<LoggedInUser>>> signIn(@Valid @RequestBody JwtAuthenticationRequest authenticationRequest) {
		return this.authService.signIn(authenticationRequest);
	}

	@PostMapping(
					value = "/signup",
					consumes =  MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity> signUp(@Valid @RequestBody User user) {
		return this.authService.signUp(user);
	}

	@GetMapping(
					value = "/me",
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getAuthenticatedUser(@RequestHeader(value = "Authorization") String bearerToken) {
		return this.authService.getAuthenticatedUser(bearerToken);
	}
}