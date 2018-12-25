package com.robotrade.robotradeapi.rabbitMQ.prod.transactionHistory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.rabbitMQ.constants.TransactionHistoryConstants;
import com.robotrade.robotradeapi.rabbitMQ.models.TransactionHistoryFromPython;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// TODO: Currently, we dont use this transaction history client because to User contains all the transactions but it may change in the future
@NoArgsConstructor
@Slf4j
public class TransactionHistoryClient {

	@Autowired
	private RabbitTemplate cloudRabbitTemplate;

	@Autowired
	private DirectExchange transactionHistoryExchange;

	public TransactionHistoryClient(RabbitTemplate cloudRabbitTemplate, DirectExchange transactionHistoryExchange) {
		this.cloudRabbitTemplate = cloudRabbitTemplate;
		this.transactionHistoryExchange = transactionHistoryExchange;
	}

	public void requestTransactionHistory() {
		ObjectMapper mapper = new ObjectMapper();
		String response = (String) cloudRabbitTemplate.convertSendAndReceive(this.transactionHistoryExchange.getName(), TransactionHistoryConstants.TRANSACTION_HISTORY_REQUEST_ROUTING_KEY, "Requesting transaction history");
		log.info(" *** TRANSACTION HISTORY RECEIVED: *** ");
		log.info(response);

		try {
			List<TransactionHistoryFromPython> transactionHistoryFromPython = mapper.readValue(response, new TypeReference<List<TransactionHistoryFromPython>>(){});
		} catch (Exception e) {
			log.info("An error occurred while trying to read JSON!");
			e.printStackTrace();
		}
	}
}
