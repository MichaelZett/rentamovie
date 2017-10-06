package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

public interface RentService {

	Rent createRent(Movie movie, Customer customer, LocalDate startDate);
}
