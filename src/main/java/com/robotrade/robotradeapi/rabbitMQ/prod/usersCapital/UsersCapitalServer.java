package com.robotrade.robotradeapi.rabbitMQ.prod.usersCapital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotrade.robotradeapi.rabbitMQ.constants.UserCapitalConstants;
import com.robotrade.robotradeapi.rabbitMQ.models.AllUsersCapital;
import com.robotrade.robotradeapi.service.TransactionsCalculationService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@NoArgsConstructor
public class UsersCapitalServer {

	@Autowired
	private TransactionsCalculationService transactionsCalculationService;

	public UsersCapitalServer(TransactionsCalculationService transactionsCalculationService) {
		this.transactionsCalculationService= transactionsCalculationService;
	}

	@RabbitListener(queues = UserCapitalConstants.USERS_CAPITAL_QUEUE_NAME, admin = "cloudAMQPAdmin", containerFactory = "rabbitListenerContainerFactory")
	private String sendAllUsersCapital(String message) {

		log.info("Sending TraderBotTransaction History!");
		// TODO: try to find a way to get the value without blocking the stream
		AllUsersCapital usersCapital = this.transactionsCalculationService.getAllUsersCapital().block();
		log.info("*** Users Capital arrived! ***");
		log.info(usersCapital.toString());

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(usersCapital);
		} catch (Exception e) {
			log.info("An error occurred while trying to convert POJO to JSON! returning: { capital: 0.00 }");
			e.printStackTrace();
		}

		return new AllUsersCapital(0.00).toString();
	}

}
