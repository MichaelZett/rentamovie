package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;

public interface RateService {
	public Rate retrieveRateByAge(int age);
}
