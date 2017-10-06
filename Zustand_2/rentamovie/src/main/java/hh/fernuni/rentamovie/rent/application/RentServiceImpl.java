package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.application.MovieServiceImpl;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

class RentServiceImpl implements RentService {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
	private MovieService movieService = new MovieServiceImpl();

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Copy copy = this.movieService.findCopy(movie);
		return new Rent(ID_GENERATOR.getAndIncrement(), customer, copy, startDate);
	}

}
