package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.rabbitMQ.models.AllUsersCapital;
import com.robotrade.robotradeapi.rabbitMQ.models.Transaction;
import com.robotrade.robotradeapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

	// This is just a mock transaction history loader atm
	public void distributeTransaction(Transaction transaction) {
		this.userRepository
						.findAll()
						.collectList()
						.publishOn(Schedulers.parallel())
						.flatMap(users -> Mono.just(users.stream()
										.map(user -> {
											List<Transaction> existingTransactions = user.getTransactionHistory();
											existingTransactions.add(transaction);
											user.setTransactionHistory(existingTransactions);
											return user;
										})
										.collect(Collectors.toList()))
						)
						.flatMap(users -> this.userRepository.saveAll(users).collectList())
						.subscribe(users -> log.info("Modified users saved!"));
	}
}
