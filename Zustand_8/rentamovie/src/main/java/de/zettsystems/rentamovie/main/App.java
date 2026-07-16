package de.zettsystems.rentamovie.main;

import de.zettsystems.rentamovie.customer.application.CustomerService;
import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rate.application.RateService;
import de.zettsystems.rentamovie.rate.domain.Rate;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;

public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

	public static void main(String[] args) {
		LOG.info("App was started");
		CustomerService customerService = CustomerService.getService();
		MovieService movieService = MovieService.getService();
		RentService rentService = RentService.getService();
		RateService rateService = RateService.getService();

		LOG.info("Loaded from disk: {} movies, {} customers, {} rents.", movieService.readAllMovies().size(),
				customerService.readAllCustomers().size(), rentService.readAllRents().size());

		Movie aNewHope = movieService.createMovie(Year.of(1977), "A new hope");
		movieService.createCopies(aNewHope, 1);
		LOG.info("{} was created.", aNewHope);

		Customer customer = customerService.createCustomer("Luke", "Skywalker", LocalDate.of(1951, 9, 25));
		LocalDate today = LocalDate.now(SYSTEM_ZONE);
		Rent rent = rentService.createRent(aNewHope, customer, today);
		LOG.info("{} was created. Open rents: {}", rent, rentService.findOpenRents().size());

		try {
			rentService.createRent(aNewHope, customer, today);
		} catch (IllegalStateException e) {
			LOG.info("Validation prevented renting the same copy twice: {}", e.getMessage());
		}

		rentService.returnRent(rent, today);
		LOG.info("{} was returned. Open rents: {}", rent, rentService.findOpenRents().size());

		try {
			rentService.returnRent(rent, today);
		} catch (IllegalStateException e) {
			LOG.info("Validation prevented a second return: {}", e.getMessage());
		}

		int age = Period.between(customer.getBirthdate(), rent.getEndDate()).getYears();
		Rate rate = rateService.retrieveRateByAge(age);
		BigDecimal price = rateService.calculatePrice(rent, rate);
		LOG.info("Price for the rent at rate {}: {}", rate, price);

		try {
			rentService.payRent(rent, BigDecimal.ZERO);
		} catch (IllegalArgumentException e) {
			LOG.info("Validation rejected the payment: {}", e.getMessage());
		}

		rentService.payRent(rent, price);
		LOG.info("Rents with open payment: {}", rentService.findRentsWithOpenPayment().size());

		LOG.info("Stored on disk: {} movies, {} customers, {} rents.", movieService.readAllMovies().size(),
				customerService.readAllCustomers().size(), rentService.readAllRents().size());
	}

}
