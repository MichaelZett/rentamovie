package de.zettsystems.rentamovie.customer.application;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import de.zettsystems.rentamovie.customer.domain.Customer;

public class CustomerServiceImpl implements CustomerService {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);

	@Override
	public Customer createCustomer(String firstname, String lastname, LocalDate birthday) {
		return new Customer(ID_GENERATOR.getAndIncrement(), firstname, lastname, birthday);
	}

	@Override
	public void updateCustomer(Customer currentCustomer, String firstname, String lastname, LocalDate birthdate) {
		currentCustomer.updateData(firstname, lastname, birthdate);
	}

}
