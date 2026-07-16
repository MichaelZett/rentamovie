package de.zettsystems.rentamovie.customer.application;

import de.zettsystems.rentamovie.customer.domain.Customer;

import java.time.LocalDate;
import java.util.Collection;

public interface CustomerService {
	Customer createCustomer(String firstname, String lastname, LocalDate birthday);

	void updateCustomer(Customer currentCustomer, String firstname, String lastname, LocalDate birthdate);

	Collection<Customer> readAllCustomers();

    Collection<Customer> readActiveCustomers();

	static CustomerService getService() {
		return CustomerServiceImpl.getInstance();
	}
}
