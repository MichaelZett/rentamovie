package hh.fernuni.rentamovie.customer.application;

import java.time.LocalDate;

import hh.fernuni.rentamovie.customer.domain.Customer;

class CustomerServiceImpl implements CustomerService {
	private static final CustomerService INSTANCE = new CustomerServiceImpl();

	private CustomerServiceImpl() {
		// should only be called from within this class
	}
	
	static CustomerService getInstance() {
		return INSTANCE;
	}

	@Override
	public Customer createCustomer(String surename, String lastname, LocalDate birthday) {
		return new Customer(surename, lastname, birthday);
	}

	@Override
	public void updateCustomers(Customer currentUser, String firstname, String lastname, LocalDate birthdate) {
		currentUser.updateData(firstname, lastname, birthdate);
	}

}
