package de.zettsystems.rentamovie.rate.application;

import de.zettsystems.rentamovie.rate.domain.Rate;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.math.BigDecimal;

public interface RateService {
	Rate retrieveRateByAge(int age);

    BigDecimal calculatePrice(Rent rent, Rate rate);

	static RateService getService() {
		return RateServiceImpl.getInstance();
	}
}
