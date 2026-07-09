package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

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

    void returnRent(Rent rent, LocalDate endDate);

    void payRent(Rent rent, BigDecimal amount);

    List<Rent> findOpenRents();

    List<Rent> findOverdueRents(LocalDate date);

    List<Rent> findRentsWithOpenPayment();
}
