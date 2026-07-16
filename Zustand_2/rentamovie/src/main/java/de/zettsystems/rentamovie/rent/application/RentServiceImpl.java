package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.application.MovieServiceImpl;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;

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
        validateCustomer(customer);
        validateMovie(movie);
		Copy copy = this.movieService.findCopy(movie);
        validateCopy(copy);
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

    private static void validateCustomer(Customer customer) {
        if (!customer.isActive()) {
            throw new IllegalStateException("Customer is not active.");
        }
    }

    private static void validateMovie(Movie movie) {
        if (!movie.isActive()) {
            throw new IllegalStateException("Movie is not active.");
        }
    }

    private static void validateCopy(Copy copy) {
        if (!copy.isAvailable() || !copy.getMovie().isActive()) {
            throw new IllegalStateException("Copy is not available.");
        }
    }
}
