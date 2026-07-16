package de.zettsystems.rentamovie.customer.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.customer.domain.CustomerRepository;

import java.time.LocalDate;
import java.util.Collection;

class CustomerServiceImpl implements CustomerService {
    private static final CustomerService INSTANCE = new CustomerServiceImpl();
    private CustomerRepository customerRepository = CustomerRepository.getRepository();

    private CustomerServiceImpl() {
        // should only be called from within this class
    }

    static CustomerService getInstance() {
        return INSTANCE;
    }

    @Override
    public Customer createCustomer(String firstname, String lastname, LocalDate birthday) {
        Customer customer = new Customer(firstname, lastname, birthday);
        this.customerRepository.save(customer);
        return customer;
    }

    @Override
    public void updateCustomer(Customer currentCustomer, String firstname, String lastname, LocalDate birthdate) {
        currentCustomer.updateData(firstname, lastname, birthdate);
        this.customerRepository.save(currentCustomer);
    }

    @Override
    public Collection<Customer> readAllCustomers() {
        return this.customerRepository.readAll();
    }

    @Override
    public Collection<Customer> readActiveCustomers() {
        return this.customerRepository.readAll().stream()
                .filter(Customer::isActive)
                .toList();
    }

}
