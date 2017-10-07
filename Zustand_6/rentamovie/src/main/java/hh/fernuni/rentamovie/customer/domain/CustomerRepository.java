package hh.fernuni.rentamovie.customer.domain;

import java.util.Collection;

public interface CustomerRepository {
	static CustomerRepository getRepository() {
		return CustomerRepositoryImpl.getInstance();
	}

	void save(Customer customer);

	Customer read(Long id);

	Collection<Customer> readAll();
}
