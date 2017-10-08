package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;
import java.util.Collection;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

class RentServiceImpl implements RentService {
	private static final RentService Instance = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();

	private RentServiceImpl() {
		// should only be called from within this class
	}

	static RentService getInstance() {
		return Instance;
	}

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Collection<Copy> copy = this.movieService.findAllCopiesOfMovie(movie);
		return new Rent(customer, copy.iterator().next(), startDate);
	}

}
