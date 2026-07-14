package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rate.application.RateService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class RentServiceImpl implements RentService {
	private static final RentService Instance = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();
	private RentRepository rentRepository = RentRepository.getRepository();
	private RateService rateService = RateService.getService();

	private RentServiceImpl() {
		// should only be called from within this class
	}

	static RentService getInstance() {
		return Instance;
	}

	@Override
	public Collection<Copy> findAllFreeCopies(Movie movie) {
        validateMovie(movie);
        Collection<Copy> allCopies = new ArrayList<>(this.movieService.findAllCopiesOfMovie(movie));
		Set<Copy> rentedCopies = this.rentRepository.readAll().stream()
                .filter(r -> r.isOpen() && allCopies.contains(r.getCopy())).map(Rent::getCopy)
		        .collect(Collectors.toSet());
		allCopies.removeAll(rentedCopies);
        return allCopies.stream().filter(Copy::isAvailable).toList();
	}

	@Override
	public Rent createRent(Movie movie, Customer customer, LocalDate startDate) {
        validateCustomer(customer);
        Collection<Copy> copies = findAllFreeCopies(movie);
        if (copies.isEmpty()) {
            throw new IllegalStateException("No free copy available.");
        }
		Rent newRent = new Rent(customer, copies.iterator().next(), startDate);
		this.rentRepository.save(newRent);
		return newRent;
	}

    @Override
    public void returnRent(Rent rent, LocalDate endDate) {
        if (rent.isFinished()) {
            throw new IllegalStateException("Rent is already returned.");
        }
        if (endDate.isBefore(rent.getStartDate())) {
            throw new IllegalArgumentException("Return date must not be before start date.");
        }
        rent.endRent(endDate);
        this.rentRepository.save(rent);
    }

    @Override
    public void payRent(Rent rent, BigDecimal amount) {
        if (rent.isOpen()) {
            throw new IllegalStateException("Rent must be returned before payment.");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        BigDecimal expectedAmount = expectedPaymentAmount(rent);
        if (amount.compareTo(expectedAmount) != 0) {
            throw new IllegalArgumentException("Payment amount must be " + expectedAmount + ".");
        }
        rent.markPaid();
        this.rentRepository.save(rent);
    }

    private BigDecimal expectedPaymentAmount(Rent rent) {
        int age = Period.between(rent.getUser().getBirthdate(), rent.getEndDate()).getYears();
        return this.rateService.calculatePrice(rent, this.rateService.retrieveRateByAge(age));
    }

    @Override
    public Collection<Rent> readAllRents() {
        return this.rentRepository.readAll();
    }

    @Override
    public List<Rent> findOpenRents() {
        return this.rentRepository.readAll().stream()
                .filter(Rent::isOpen)
                .toList();
    }

    @Override
    public List<Rent> findOverdueRents(LocalDate date) {
        return this.rentRepository.readAll().stream()
                .filter(r -> r.isOverdue(date))
                .toList();
    }

    @Override
    public List<Rent> findRentsWithOpenPayment() {
        return this.rentRepository.readAll().stream()
                .filter(Rent::hasOpenPayment)
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

}
