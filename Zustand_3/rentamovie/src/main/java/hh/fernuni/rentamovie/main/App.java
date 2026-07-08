package hh.fernuni.rentamovie.main;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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

		Movie aNewHope = movieService.createMovie(Year.of(1977), "A new hope");
		LOG.info("{} was created.", aNewHope);

		Customer customer = customerService.createCustomer("Luke", "Skywalker", LocalDate.of(1951, 9, 25));
		Rent rent = rentService.createRent(aNewHope, customer, LocalDate.now(SYSTEM_ZONE));
		LOG.info("{} was created. Open rents: {}", rent, rentService.findOpenRents().size());

		rentService.returnRent(rent);
		LOG.info("{} was returned. Open rents: {}", rent, rentService.findOpenRents().size());
	}

}
