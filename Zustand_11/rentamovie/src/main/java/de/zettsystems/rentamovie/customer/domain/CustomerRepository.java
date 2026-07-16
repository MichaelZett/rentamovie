package de.zettsystems.rentamovie.customer.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepository;

public interface CustomerRepository extends CommonRepository<Customer> {
    static CustomerRepository getRepository() {
        return CustomerRepositoryImpl.getInstance();
    }

}
