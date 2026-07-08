package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class RentServiceImpl implements RentService {
	private static final RentService Instance = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();
    private List<Rent> rents = new ArrayList<>();

	private RentServiceImpl() {
		// should only be called from within this class
	}

	static RentService getInstance() {
		return Instance;
	}

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Copy copy = this.movieService.findCopy(movie);
        Rent rent = new Rent(customer, copy, startDate);
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
