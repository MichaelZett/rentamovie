package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RentService {
	static RentService getService() {
		return RentServiceImpl.getInstance();
	}

	Rent createRent(Movie movie, Customer customer, LocalDate startDate);

    void returnRent(Rent rent);

    void payRent(Rent rent);

    Collection<Rent> readAllRents();

    List<Rent> findOpenRents();

    List<Rent> findOverdueRents(LocalDate date);

    List<Rent> findRentsWithOpenPayment();
}
