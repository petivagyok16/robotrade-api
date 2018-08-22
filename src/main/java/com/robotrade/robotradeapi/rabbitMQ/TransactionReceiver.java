package com.robotrade.robotradeapi.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.rabbitMQ.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class TransactionReceiver {

	@RabbitListener(queues = RabbitConstants.ROBO_TRANSACTION_QUEUE_NAME)
	public void receiveTransaction(String transaction) {
		this.receive(transaction);
	}

	private void receive(String transactionString) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			Transaction transaction = mapper.readValue(transactionString, Transaction.class);
			log.info(" ****** TRANSACTION RECEIVED: ***** ");
			log.info(transaction.toString());
		} catch (Exception e) {
			log.info(" --- Error during processing JSON value from Rabbit --- ");
			e.printStackTrace();
		}
	}

}
