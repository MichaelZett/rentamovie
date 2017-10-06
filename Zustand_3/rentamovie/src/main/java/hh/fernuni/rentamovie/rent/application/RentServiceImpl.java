package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

public class RentServiceImpl implements RentService {
	private static final RentService Instance = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();

	public static RentService getInstance() {
		return Instance;
	}

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Copy copy = this.movieService.findCopy(movie);
		return new Rent(customer, copy, startDate);
	}

}
