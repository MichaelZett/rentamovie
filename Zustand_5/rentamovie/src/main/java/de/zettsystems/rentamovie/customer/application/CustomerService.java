package de.zettsystems.rentamovie.customer.application;

import java.time.LocalDate;
import java.util.Collection;

import de.zettsystems.rentamovie.customer.domain.Customer;

public interface CustomerService {
	Customer createCustomer(String firstname, String lastname, LocalDate birthday);

	void updateCustomer(Customer currentCustomer, String firstname, String lastname, LocalDate birthdate);

	Collection<Customer> readAllCustomers();

	static CustomerService getService() {
		return CustomerServiceImpl.getInstance();
	}
}
