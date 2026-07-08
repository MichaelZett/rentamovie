package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.application.MovieServiceImpl;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RentServiceImpl implements RentService {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
	private MovieService movieService = new MovieServiceImpl();
    private List<Rent> rents = new ArrayList<>();

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Copy copy = this.movieService.findCopy(movie);
        Rent rent = new Rent(ID_GENERATOR.getAndIncrement(), customer, copy, startDate);
        this.rents.add(rent);
        return rent;
    }

    @Override
    public void returnRent(Rent rent) {
        rent.endRent();
    }

    @Override
    public List<Rent> findOpenRents() {
        return this.rents.stream()
                .filter(Rent::isValid)
                .toList();
	}

}
