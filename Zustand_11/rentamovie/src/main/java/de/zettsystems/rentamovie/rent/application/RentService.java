package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RentService {
    static RentService getService() {
        return RentServiceImpl.getInstance();
    }

    Rent createRent(Movie movie, Customer customer, LocalDate startDate);

    Collection<Copy> findAllFreeCopies(Movie movie);

    Collection<Rent> readAllRents();

    Rent createRent(Copy value, Customer value2, LocalDate now);

    Rent createRent(Copy value, Customer value2, LocalDate now, int plannedDays);

    void returnRent(Rent rent, LocalDate endDate);

    void payRent(Rent rent, BigDecimal amount);

    List<Rent> findOpenRents();

    List<Rent> findOverdueRents(LocalDate date);

    List<Rent> findRentsWithOpenPayment();

    List<Rent> findPaymentsOn(LocalDate paymentDate);

    BigDecimal sumPaymentsOn(LocalDate paymentDate);
}
