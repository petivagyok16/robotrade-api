package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.common.ExceptionThrower;
import com.robotrade.robotradeapi.models.HttpResponseWrapper;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(
					UserRepository userRepository
	) {
		this.userRepository = userRepository;
	}

	public Mono<ResponseEntity<HttpResponseWrapper<List<User>>>> getAllUser() {
		return this.userRepository.findAll().collectList()
						.filter(users -> users.size() > 0)
						.map(users -> ResponseEntity.ok(new HttpResponseWrapper<>(users)))
						.defaultIfEmpty(ResponseEntity.noContent().build());
	}

	public Mono<ResponseEntity<HttpResponseWrapper<User>>> getUserProfile(String id) {
		return this.userRepository.findById(id)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(user))));
	}
}
