package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;

public interface RateService {
	Rate retrieveRateByAge(int age);

	static RateService getService() {
		return RateServiceImpl.getInstance();
	}
}
