package de.zettsystems.rentamovie.rate.application;

import de.zettsystems.rentamovie.rate.domain.Rate;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

class RateServiceImpl implements RateService {
    private static final RateService INSTANCE = new RateServiceImpl();

    private RateServiceImpl() {
        // should only be called from within this class
    }

    static RateService getInstance() {
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

    @Override
    public BigDecimal calculatePrice(Rent rent, Rate rate) {
        if (rent.isOpen()) {
            throw new IllegalStateException("Rent must be returned before the price can be calculated.");
        }
        long days = Math.max(1L, ChronoUnit.DAYS.between(rent.getStartDate(), rent.getEndDate()));
        return rate.getValue().multiply(BigDecimal.valueOf(days));
    }

}
