package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;

public class RateServiceImpl implements RateService {

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
