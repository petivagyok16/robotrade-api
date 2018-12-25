package com.robotrade.robotradeapi.rabbitMQ.dev.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.models.TraderBotTransaction;
import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@NoArgsConstructor
public class TransactionReceiver {

	@RabbitListener(queues = TransactionConstants.ROBO_TRANSACTION_QUEUE_NAME)
	public void receiveTransaction(String transaction) {
		this.receive(transaction);
	}

	private void receive(String transactionString) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			TraderBotTransaction transaction = mapper.readValue(transactionString, TraderBotTransaction.class);
			log.info(" ****** TRANSACTION RECEIVED: ***** ");
			log.info(transaction.toString());
		} catch (Exception e) {
			log.info(" --- Error during processing JSON value from Rabbit --- ");
			e.printStackTrace();
		}
	}

}

