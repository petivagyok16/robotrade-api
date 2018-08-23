package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.common.ExceptionThrower;
import com.robotrade.robotradeapi.converters.UserToPortfolio;
import com.robotrade.robotradeapi.models.HttpResponseWrapper;
import com.robotrade.robotradeapi.models.Portfolio;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.rabbitMQ.models.AllUsersCapital;
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
	public UserService(UserRepository userRepository) {
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

	public Mono<ResponseEntity<HttpResponseWrapper<Portfolio>>> getUserPortfolio(String id) {
		return this.userRepository.findById(id)
						.single()
						.doOnError(ExceptionThrower::userNotFound)
						.flatMap(user -> {
							Portfolio userPortfolio = new UserToPortfolio().convert(user);
							return Mono.just(ResponseEntity.ok().body(new HttpResponseWrapper<>(userPortfolio)));
						});
	}

	public Mono<AllUsersCapital> getAllUsersCapital() {
		return this.userRepository.findAll()
						.map(User::getCash)
						.reduce(0.00, (c1, c2) -> c1 + c2)
						.flatMap(capital -> Mono.just(new AllUsersCapital(capital)));
	}
}
