package hh.fernuni.rentamovie.customer.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepositoryImpl;

import java.time.LocalDate;

class CustomerRepositoryImpl extends CommonRepositoryImpl<Customer> implements CustomerRepository {
    private static final CustomerRepositoryImpl INSTANCE = new CustomerRepositoryImpl("./customer.db");

    private CustomerRepositoryImpl(String filename) {
        super(filename);
    }

    @Override
    protected Customer fromText(String[] split) {
        CustomerStatus status = split.length > 4 ? CustomerStatus.valueOf(split[4]) : CustomerStatus.ACTIVE;
        return new Customer(Long.parseLong(split[0]), split[1], split[2], LocalDate.parse(split[3]), status);
    }

    @Override
    protected String toText(Customer user) {
        StringBuilder b = new StringBuilder();
        b.append(user.getId()).append(DELIMITER);
        b.append(user.getFirstname()).append(DELIMITER);
        b.append(user.getLastname()).append(DELIMITER);
        b.append(user.getBirthdate()).append(DELIMITER);
        b.append(user.getStatus());
        return b.toString();
    }

    static CustomerRepository getInstance() {
        return INSTANCE;
    }

}
