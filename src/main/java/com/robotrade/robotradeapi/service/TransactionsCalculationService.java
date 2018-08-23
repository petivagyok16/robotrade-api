package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.rabbitMQ.models.AllUsersCapital;
import com.robotrade.robotradeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionsCalculationService {
	private final UserRepository userRepository;

	@Autowired
	public TransactionsCalculationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Mono<AllUsersCapital> getAllUsersCapital() {
		return this.userRepository.findAll()
						.map(User::getCash)
						.reduce(0.00, (c1, c2) -> c1 + c2)
						.flatMap(capital -> Mono.just(new AllUsersCapital(capital)));
	}
}
