package de.zettsystems.rentamovie.customer.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepositoryImpl;

import java.time.LocalDate;

class CustomerRepositoryImpl extends CommonRepositoryImpl<Customer> implements CustomerRepository {
	private static final CustomerRepositoryImpl INSTANCE = new CustomerRepositoryImpl(System.getProperty("rentamovie.customer.db", "./customer.db"));

	private CustomerRepositoryImpl(String filename) {
		super(filename);
	}

	@Override
	protected Customer fromText(String[] split) {
        CustomerStatus status = split.length > 4 ? CustomerStatus.valueOf(split[4]) : CustomerStatus.ACTIVE;
        return new Customer(Long.parseLong(split[0]), split[1], split[2], LocalDate.parse(split[3]), status);
	}

	@Override
	protected String toText(Customer customer) {
		StringBuilder b = new StringBuilder();
		b.append(customer.getId()).append(DELIMITER);
		b.append(requireStorableText(customer.getFirstname())).append(DELIMITER);
		b.append(requireStorableText(customer.getLastname())).append(DELIMITER);
        b.append(customer.getBirthdate()).append(DELIMITER);
        b.append(customer.getStatus());
		return b.toString();
	}

	static CustomerRepository getInstance() {
		return INSTANCE;
	}

}
