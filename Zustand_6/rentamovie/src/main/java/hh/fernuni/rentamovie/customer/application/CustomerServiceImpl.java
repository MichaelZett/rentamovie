package hh.fernuni.rentamovie.customer.application;

import java.time.LocalDate;
import java.util.Collection;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;

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
	public Customer createCustomer(String surename, String lastname, LocalDate birthday) {
		Customer customer = new Customer(surename, lastname, birthday);
		this.customerRepository.save(customer);
		return customer;
	}

	@Override
	public void updateCustomers(Customer currentUser, String firstname, String lastname, LocalDate birthdate) {
		currentUser.updateData(firstname, lastname, birthdate);
		this.customerRepository.save(currentUser);
	}

	@Override
	public Collection<Customer> readAllCustomers() {
		return this.customerRepository.readAll();
	}

}
