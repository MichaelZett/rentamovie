package hh.fernuni.rentamovie.customer.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepository;

public interface CustomerRepository extends CommonRepository<Customer> {
	static CustomerRepository getRepository() {
		return CustomerRepositoryImpl.getInstance();
	}

}
