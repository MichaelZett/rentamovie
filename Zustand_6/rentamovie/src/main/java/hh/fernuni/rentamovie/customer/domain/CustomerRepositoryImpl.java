package hh.fernuni.rentamovie.customer.domain;

import java.util.Collection;

class CustomerRepositoryImpl implements CustomerRepository {
	private static final CustomerRepositoryImpl INSTANCE = new CustomerRepositoryImpl();

	private CustomerRepositoryImpl() {
		// only used in thid class
	}

	static CustomerRepository getInstance() {
		return INSTANCE;
	}

	@Override
	public void save(Customer customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Customer read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Customer> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
