package de.zettsystems.rentamovie.customer.domain;

import org.jspecify.annotations.Nullable;
import java.util.Collection;

public interface CustomerRepository {
	static CustomerRepository getRepository() {
		return CustomerRepositoryImpl.getInstance();
	}

	void save(Customer customer);

	@Nullable Customer read(Long id);

	Collection<Customer> readAll();
}
