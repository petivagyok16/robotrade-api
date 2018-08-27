package com.robotrade.robotradeapi.rabbitMQ.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionConstants;
import com.robotrade.robotradeapi.rabbitMQ.models.Transaction;
import com.robotrade.robotradeapi.service.TransactionsCalculationService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
@NoArgsConstructor
public class TransactionReceiver {

	@Autowired
	private TransactionsCalculationService transactionsCalculationService;

	public TransactionReceiver(TransactionsCalculationService transactionsCalculationService) {
		this.transactionsCalculationService = transactionsCalculationService;
	}

	@RabbitListener(queues = TransactionConstants.ROBO_TRANSACTION_QUEUE_NAME)
	public void receiveTransaction(String transaction) {
		this.receive(transaction);
	}

	private void receive(String transactionString) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			Transaction transaction = mapper.readValue(transactionString, Transaction.class);
			log.info(" ****** TRANSACTION RECEIVED: ***** ");
			log.info(transaction.toString());
			transaction.setId(UUID.randomUUID().toString());
			this.transactionsCalculationService.distributeTransaction(transaction);

		} catch (Exception e) {
			log.info(" --- Error during processing JSON value from Rabbit --- ");
			e.printStackTrace();
		}
	}

}
