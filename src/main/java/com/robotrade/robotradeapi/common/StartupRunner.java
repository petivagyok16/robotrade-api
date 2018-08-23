package com.robotrade.robotradeapi.common;

import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.repository.UserRepository;
import com.robotrade.robotradeapi.security.CustomPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("localDB")
public class StartupRunner implements CommandLineRunner {

	private final UserRepository userRepository;
	private final CustomPasswordEncoder passwordEncoder;

	public StartupRunner(
					UserRepository userRepository,
					CustomPasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(" ------- Loading dummy data into MongoDB ------- ");
		this.addUser();
	}

	private void addUser() {
		User testUser = new User("1", "test", this.passwordEncoder.encode("test"), 5.00);
		testUser.setInitialInvestment(testUser.getCash());
		this.userRepository.save(testUser).subscribe();
	}
}
