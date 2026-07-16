package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rate.application.RateService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import de.zettsystems.rentamovie.rent.domain.RentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class RentServiceImpl implements RentService {
	private static final RentService INSTANCE = new RentServiceImpl();
	private MovieService movieService = MovieService.getService();
	private RentRepository rentRepository = RentRepository.getRepository();
	private RateService rateService = RateService.getService();

	private RentServiceImpl() {
		// should only be called from within this class
	}

	static RentService getInstance() {
		return INSTANCE;
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
        return createRent(copies.iterator().next(), customer, startDate, 7);
	}

	@Override
	public Collection<Rent> readAllRents() {
		return this.rentRepository.readAll();
	}

	@Override
	public Rent createRent(Copy copy, Customer customer, LocalDate startDate) {
        return createRent(copy, customer, startDate, 7);
    }

    @Override
    public Rent createRent(Copy copy, Customer customer, LocalDate startDate, int plannedDays) {
        validateCustomer(customer);
        validateCopy(copy);
        if (plannedDays < 1 || plannedDays > 7) {
            throw new IllegalArgumentException("Planned rental duration must be between 1 and 7 days.");
        }
        if (this.rentRepository.readAll().stream().anyMatch(r -> r.isOpen() && r.getCopy().equals(copy))) {
            throw new IllegalStateException("Copy is already rented.");
        }
        Rent newRent = new Rent(customer, copy, startDate, startDate.plusDays(plannedDays));
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
        int age = Period.between(rent.getCustomer().getBirthdate(), rent.getEndDate()).getYears();
        return this.rateService.calculatePrice(rent, this.rateService.retrieveRateByAge(age));
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

    private static void validateCopy(Copy copy) {
        if (!copy.isAvailable() || !copy.getMovie().isActive()) {
            throw new IllegalStateException("Copy is not available.");
        }
    }
}
