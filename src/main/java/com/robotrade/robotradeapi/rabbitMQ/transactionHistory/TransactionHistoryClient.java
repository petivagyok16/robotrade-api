package com.robotrade.robotradeapi.rabbitMQ.transactionHistory;

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

@NoArgsConstructor
@Slf4j
public class TransactionHistoryClient {

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private DirectExchange exchange;

	public void requestTransactionHistory() {
		ObjectMapper mapper = new ObjectMapper();
		String response = (String) template.convertSendAndReceive(this.exchange.getName(), TransactionHistoryConstants.TRANSACTION_HISTORY_REQUEST_ROUTING_KEY, "Requesting transaction history");
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
