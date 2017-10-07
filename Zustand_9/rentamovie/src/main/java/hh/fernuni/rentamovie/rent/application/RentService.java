package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;
import java.util.Collection;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

public interface RentService {
	static RentService getService() {
		return RentServiceImpl.getInstance();
	}

	Rent createRent(Movie movie, Customer customer, LocalDate startDate);

	Collection<Copy> findAllFreeCopies(Movie movie);

	Collection<Rent> readAllRents();

	Rent createRent(Copy value, Customer value2, LocalDate now);
}
