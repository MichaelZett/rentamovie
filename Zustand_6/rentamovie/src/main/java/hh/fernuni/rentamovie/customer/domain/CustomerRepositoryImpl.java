package hh.fernuni.rentamovie.customer.domain;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CustomerRepositoryImpl implements CustomerRepository {
	private static final CustomerRepositoryImpl INSTANCE = new CustomerRepositoryImpl();
	private final Map<Long, Customer> repo = new ConcurrentHashMap<>();

	private CustomerRepositoryImpl() {
		// only used in this class
	}

	static CustomerRepository getInstance() {
		return INSTANCE;
	}

	@Override
	public void save(Customer customer) {
		this.repo.put(customer.getId(), customer);

	}

	@Override
	public Customer read(Long id) {
		return this.repo.get(id);
	}

	@Override
	public Collection<Customer> readAll() {
		return this.repo.values();
	}

}
