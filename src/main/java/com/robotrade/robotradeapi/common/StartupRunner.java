package com.robotrade.robotradeapi.common;

import com.robotrade.robotradeapi.models.User;
import com.robotrade.robotradeapi.repository.UserRepository;
import com.robotrade.robotradeapi.security.CustomPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
		User testUser = new User("1", "test", this.passwordEncoder.encode("test"), 100.00);
		User testUser2 = new User("2", "test2", this.passwordEncoder.encode("test"), 150.00);
		User testUser3 = new User("3", "test3", this.passwordEncoder.encode("test"), 250.00);
		testUser.setStock(10.00);
		testUser2.setStock(15.00);
		testUser3.setStock(15.00);
		this.userRepository.saveAll(Arrays.asList(testUser, testUser2, testUser3)).subscribe();
	}
}
