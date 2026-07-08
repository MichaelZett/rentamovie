package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;
import hh.fernuni.rentamovie.rent.domain.Rent;

import java.math.BigDecimal;

public interface RateService {
    Rate retrieveRateByAge(int age);

    BigDecimal calculatePrice(Rent rent, Rate rate);

    static RateService getService() {
        return RateServiceImpl.getInstance();
    }
}
