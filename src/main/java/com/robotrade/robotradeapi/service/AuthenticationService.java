package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.common.ErrorMessages;
import com.robotrade.robotradeapi.exceptions.UserAlreadyExistsException;
import com.robotrade.robotradeapi.exceptions.UserNotFoundException;
import com.robotrade.robotradeapi.exceptions.WrongCredentialsException;
import com.robotrade.robotradeapi.models.HttpResponseWrapper;
import com.robotrade.robotradeapi.models.LoginResponseWrapper;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.repository.UserRepository;
import com.robotrade.robotradeapi.security.CustomPasswordEncoder;
import com.robotrade.robotradeapi.security.JwtAuthenticationRequest;
import com.robotrade.robotradeapi.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class AuthenticationService {

	private final CustomPasswordEncoder passwordEncryptor;
	private final UserRepository userRepository;
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public AuthenticationService(
					CustomPasswordEncoder passwordEncryptor,
					UserRepository userRepository,
					JwtTokenUtil jwtTokenUtil
	) {
		this.passwordEncryptor = passwordEncryptor;
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	/**
	 * Signing up a new user.
	 *
	 * @param newUser
	 *            The user to create.
	 *
	 * @return HTTP 201, the header Location contains the URL of the created
	 *         user.
	 */
	public Mono<ResponseEntity> signUp(User newUser) {

		newUser.setPassword(passwordEncryptor.encode(newUser.getPassword()));

		return this.userRepository.findUserByUsername(newUser.getUsername())
						.hasElement()
						.flatMap(exists -> {
							if (exists) {
								throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);
							} else {
								newUser.setStock(0.00);
								return this.userRepository.save(newUser).map(savedUser ->
												ResponseEntity.created(URI.create(String.format("users/%s", savedUser.getId()))).build());
							}
						});
	}

	/**
	 * Signing in user
	 *
	 * @param authenticationRequest
	 *            Credentials to create authentication
	 *
	 * @return HTTP 200, with Jwt token and user information
	 */
	public Mono<ResponseEntity<LoginResponseWrapper<User>>> signIn(JwtAuthenticationRequest authenticationRequest) {
		return this.userRepository.findUserByUsername(authenticationRequest.getUsername())
						.single()
						.doOnError(error -> {
							throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
						})
						.flatMap(user -> {

							if (this.passwordEncryptor.matches(authenticationRequest.getPassword(), user.getPassword())) {
								return Mono.just(
												ResponseEntity.ok()
																.contentType(MediaType.APPLICATION_JSON_UTF8)
																.body(new LoginResponseWrapper<>(user, this.jwtTokenUtil.generateToken(user)))
								);
							} else {
								throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
							}
						});
	}

	/**
	 * Getting the authenticated user by looking at the Jwt token
	 *
	 * @param bearerToken
	 *            Jwt token which contains some information about the user and can be used to find the user it belongs to.
	 *
	 * @return HTTP 200, with user information in payload
	 */
	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getAuthenticatedUser(String bearerToken) {
		String username = this.validateToken(bearerToken);

		return this.userRepository.findUserByUsername(username)
						.single()
						.doOnError(error -> {
							throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
						})
						.flatMap(user -> Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(user))));
	}

	/**
	 *
	 * @param bearerToken
	 * 				Jwt auth token with Bearer prefix
	 * @return username if the Token is valid
	 */
	private String validateToken(String bearerToken) {
		String authToken = this.jwtTokenUtil.formatToken(bearerToken);

		if (this.jwtTokenUtil.validateToken(authToken)) {
			return jwtTokenUtil.getUsernameFromToken(authToken);
		}

		return null;
	}
}

