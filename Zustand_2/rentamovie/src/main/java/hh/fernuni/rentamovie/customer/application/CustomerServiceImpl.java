package hh.fernuni.rentamovie.customer.application;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import hh.fernuni.rentamovie.customer.domain.Customer;

public class CustomerServiceImpl implements CustomerService {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);

	@Override
	public Customer createCustomer(String surename, String lastname, LocalDate birthday) {
		return new Customer(ID_GENERATOR.getAndIncrement(), surename, lastname, birthday);
	}

	@Override
	public void updateCustomers(Customer currentUser, String firstname, String lastname, LocalDate birthdate) {
		currentUser.updateData(firstname, lastname, birthdate);
	}

}
