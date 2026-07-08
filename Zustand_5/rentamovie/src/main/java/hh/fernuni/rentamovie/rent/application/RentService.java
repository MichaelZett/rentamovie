package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

import java.time.LocalDate;
import java.util.List;

public interface RentService {
	static RentService getService() {
		return RentServiceImpl.getInstance();
	}

	Rent createRent(Movie movie, Customer customer, LocalDate startDate);

    void returnRent(Rent rent);

    List<Rent> findOpenRents();
}
