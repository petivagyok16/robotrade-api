package com.robotrade.robotradeapi.rabbitMQ.prod.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.models.TraderBotTransaction;
import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionConstants;
import com.robotrade.robotradeapi.service.TransactionsCalculationService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@NoArgsConstructor
public class TransactionReceiver {

	@Autowired
	private TransactionsCalculationService transactionsCalculationService;

	public TransactionReceiver(TransactionsCalculationService transactionsCalculationService) {
		this.transactionsCalculationService = transactionsCalculationService;
	}

	@RabbitListener(queues = TransactionConstants.ROBO_TRANSACTION_QUEUE_NAME, admin = "cloudAMQPAdmin", containerFactory = "rabbitListenerContainerFactory")
	public void receiveTransaction(byte[] transaction) {
		this.receive(transaction);
	}

	private void receive(byte[] transaction) {
		ObjectMapper mapper = new ObjectMapper();
		TraderBotTransaction traderBotTransaction = this.parseTransaction(transaction, mapper);

		if (traderBotTransaction != null) {
			log.info(traderBotTransaction.toString());
			this.transactionsCalculationService.distributeTransaction(traderBotTransaction);
		}
	}

	private TraderBotTransaction parseTransaction(byte[] transaction, ObjectMapper mapper) {

		try {
			return mapper.readValue(transaction, TraderBotTransaction.class);
		} catch (Exception e) {
			log.info(" --- Error during processing byte[] transaction from Rabbit, trying to read it as String --- ");
			return this.parseStringTransaction(new String(transaction), mapper);
		}

	}

	private TraderBotTransaction parseStringTransaction(String transaction, ObjectMapper mapper) {

		try {
			return mapper.readValue(transaction, TraderBotTransaction.class);
		} catch (Exception e) {
			log.info("--- Error during processing JSON object from Rabbit ---");
			e.printStackTrace();
			return null;
		}
	}
}
