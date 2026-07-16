package de.zettsystems.rentamovie.customer.application;

import de.zettsystems.rentamovie.customer.domain.Customer;

import java.time.LocalDate;
import java.util.Collection;

class CustomerServiceImpl implements CustomerService {
	private static final CustomerService INSTANCE = new CustomerServiceImpl();

	private CustomerServiceImpl() {
		// should only be called from within this class
	}

	static CustomerService getInstance() {
		return INSTANCE;
	}

	@Override
	public Customer createCustomer(String firstname, String lastname, LocalDate birthday) {
		return new Customer(firstname, lastname, birthday);
	}

	@Override
	public void updateCustomer(Customer currentCustomer, String firstname, String lastname, LocalDate birthdate) {
		currentCustomer.updateData(firstname, lastname, birthdate);
	}

	@Override
	public Collection<Customer> readAllCustomers() {
        return java.util.Collections.emptyList();
	}

}
