package com.robotrade.robotradeapi.converters;

import com.robotrade.robotradeapi.models.Portfolio;
import com.robotrade.robotradeapi.models.User;
import io.micrometer.core.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class UserToPortfolio implements Converter<User, Portfolio>{

	@Synchronized
	@Nullable
	@Override
	public Portfolio convert(User user) {

		if (user == null) {
			return null;
		}

		Portfolio portfolio = new Portfolio();
		Double portfolioValue = user.getStock() + user.getCash();

		portfolio.setCash(user.getCash());
		portfolio.setStock(user.getStock());
		portfolio.setUsername(user.getUsername());
		portfolio.setPortfolio(portfolioValue);
		return portfolio;
	}
}
