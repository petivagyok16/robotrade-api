package com.robotrade.robotradeapi.security;

import com.robotrade.robotradeapi.exceptions.GetUsernameFromTokenException;
import com.robotrade.robotradeapi.exceptions.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
public class JwtAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public JwtAuthenticationConverter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public Mono<Authentication> apply(ServerWebExchange exchange) throws BadCredentialsException {
		ServerHttpRequest request = exchange.getRequest();

		try {
			String authToken = null;
			String username;

			String bearerRequestHeader = exchange.getRequest().getHeaders().getFirst(SecurityConstants.TOKEN_HEADER);

			if (bearerRequestHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				authToken = bearerRequestHeader.substring(7);
			}

			if (authToken == null && !request.getQueryParams().isEmpty()) {
				authToken = request.getQueryParams().getFirst(SecurityConstants.TOKEN_PARAM);
			}

			if (authToken == null) {
				log.warn("No token found");
				return Mono.error(new InvalidTokenException("Missing token!"));
			}

			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (IllegalArgumentException e) {
				log.error("an error occured during getting username from token", e);
				return Mono.error(new GetUsernameFromTokenException("An error occurred during validating the token!"));
			} catch (Exception e) {
				return Mono.error(new InvalidTokenException("Token is invalid!"));
			}

			log.info("checking authentication for user " + username);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				return Mono.just(new JwtPreAuthenticationToken(authToken, bearerRequestHeader, username));
			}

			return Mono.empty();
		} catch (Exception e) {
			return Mono.error(new InvalidTokenException("Failed to parse JWT token. Try again."));
		}
	}

	private String hasAuthorizationHeader(ServerHttpRequest request) {
		return request.getHeaders().getFirst(SecurityConstants.TOKEN_HEADER);
	}

	private String hasBearerPrefix(String bearerToken) {
		if (bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			return bearerToken;
		}
		return null;
	}

	private String extractToken(String bearerToken) {
		return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length());
	}

	private String getUsernameFromToken(String authToken) {
		return jwtTokenUtil.getUsernameFromToken(authToken);
	}
}
