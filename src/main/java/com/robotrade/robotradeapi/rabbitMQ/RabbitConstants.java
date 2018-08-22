package com.robotrade.robotradeapi.rabbitMQ;

class RabbitConstants {
	static final String TRANSACTION_ROUTING_KEY = "roboTrade.transaction";
	static final String TRANSACTION_HISTORY_ROUTING_KEY = "roboTrade.transactionHistory";
	static final String ROBO_TOPIC_EXCHANGE_NAME = "roboExchange";
	static final String ROBO_TRANSACTION_QUEUE_NAME = "roboTransactionQueue";
}
