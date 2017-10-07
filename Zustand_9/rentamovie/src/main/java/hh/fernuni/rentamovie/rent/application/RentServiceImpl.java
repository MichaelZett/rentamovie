package hh.fernuni.rentamovie.rent.application;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;

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
	public Collection<Copy> findAllFreeCopies(Movie movie) {
		Collection<Copy> allCopies = this.movieService.findAllCopiesOfMovie(movie);
		Set<Copy> rentedCopies = this.rentRepository.readAll().stream()
		        .filter(r -> r.isValid() && allCopies.contains(r.getCopy())).map(r -> r.getCopy())
		        .collect(Collectors.toSet());
		allCopies.removeAll(rentedCopies);
		return allCopies;
	}

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
		Collection<Copy> copies = findAllFreeCopies(movie);
		return createRent(copies.iterator().next(), customer, startDate);
	}

	@Override
	public Collection<Rent> readAllRents() {
		return this.rentRepository.readAll();
	}

	@Override
	public Rent createRent(Copy copy, Customer customer, LocalDate startDate) {
		Rent newRent = new Rent(customer, copy, startDate);
		this.rentRepository.save(newRent);
		return newRent;
	}

}
