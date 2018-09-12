package com.robotrade.robotradeapi.rabbitMQ.transactions;

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
	public void receiveTransaction(String transaction) {
		this.receive(transaction);
	}

	private void receive(String transactionString) {
		ObjectMapper mapper = new ObjectMapper();
		log.info(transactionString);

		try {
			TraderBotTransaction traderBotTransaction = mapper.readValue(transactionString, TraderBotTransaction.class);
			this.transactionsCalculationService.distributeTransaction(traderBotTransaction);

		} catch (Exception e) {
			log.info(" --- Error during processing JSON object from Rabbit --- ");
			e.printStackTrace();
		}
	}

}
