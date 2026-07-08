package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

class RentServiceImpl implements RentService {
	private static final RentService Instance = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();
    private RentRepository rentRepository = RentRepository.getRepository();

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
        this.rentRepository.save(rent);
        return rent;
    }

    @Override
    public void returnRent(Rent rent) {
        rent.endRent();
        this.rentRepository.save(rent);
    }

    @Override
    public Collection<Rent> readAllRents() {
        return this.rentRepository.readAll();
    }

    @Override
    public List<Rent> findOpenRents() {
        return this.rentRepository.readAll().stream()
                .filter(Rent::isValid)
                .toList();
	}

}
