package com.robotrade.robotradeapi.service;

import com.robotrade.robotradeapi.common.ShareCalculator;
import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.models.UserTransaction;
import com.robotrade.robotradeapi.rabbitMQ.models.AllUsersCapital;
import com.robotrade.robotradeapi.models.TraderBotTransaction;
import com.robotrade.robotradeapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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

	public void distributeTransaction(TraderBotTransaction traderBotTransaction) {
		AtomicReference<Double> latestUsersCapitalCash = new AtomicReference<>(0.00);
		AtomicReference<Double> latestUsersCapitalStock = new AtomicReference<>(0.00);

		this.userRepository
			.findAll()
			.collectList()
			.publishOn(Schedulers.parallel())
			.map(users -> {
				latestUsersCapitalCash.set(users.stream().mapToDouble(User::getCash).sum());
				latestUsersCapitalStock.set(users.stream().mapToDouble(User::getStock).sum());
				return users;
			})
			.map(users -> users
											.stream()
											.map(user -> {
												List<UserTransaction> existingTransactions = user.getTransactionHistory();
												UserTransaction userTransaction = this.calculateShare(latestUsersCapitalCash.get(), latestUsersCapitalStock.get(), user.getCash(), user.getStock(), traderBotTransaction);
												existingTransactions.add(userTransaction);
												user.setCash(userTransaction.getCash());
												user.setStock(userTransaction.getStock());
												user.setTransactionHistory(existingTransactions);
												return user;
											})
											.collect(Collectors.toList()))
			.map(this.userRepository::saveAll)
			.subscribe(fluxUsers -> log.info("New traderBotTransaction added to users!"));
	}
	// TODO: refactor
	private UserTransaction calculateShare(double latestUsersCapitalCash, double latestUsersCapitalStock, double userCash, double userStock, TraderBotTransaction traderBotTransaction) {
		double cashMargin = ShareCalculator.calculateMargin(userCash, latestUsersCapitalCash);
		double stockMargin = ShareCalculator.calculateMargin(userStock, latestUsersCapitalStock);
		double cashShare = ShareCalculator.calculateShare(traderBotTransaction.getCash(), cashMargin);
		double stockShare = ShareCalculator.calculateShare(traderBotTransaction.getStock(), stockMargin);

		return new UserTransaction(UUID.randomUUID().toString(), traderBotTransaction.getType(), cashShare, stockShare, traderBotTransaction.getDate());
	}
}
