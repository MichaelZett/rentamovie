package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;

public class RateServiceImpl implements RateService {
	private static final RateService INSTANCE = new RateServiceImpl();

	public static RateService getInstance() {
		return INSTANCE;
	}

	@Override
	public Rate retrieveRateByAge(int age) {
		if (age <= 12) {
			return Rate.JUNIOR;
		} else if (age >= 65) {
			return Rate.SENIOR;
		} else {
			return Rate.REGULAR;
		}
	}

}
