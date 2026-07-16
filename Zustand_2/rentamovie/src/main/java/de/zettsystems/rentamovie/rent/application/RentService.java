package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.time.LocalDate;
import java.util.List;

public interface RentService {

	Rent createRent(Movie movie, Customer customer, LocalDate startDate);

    void returnRent(Rent rent);

    List<Rent> findOpenRents();
}
