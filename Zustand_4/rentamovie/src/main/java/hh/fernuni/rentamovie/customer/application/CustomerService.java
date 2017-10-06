package hh.fernuni.rentamovie.customer.application;

import java.time.LocalDate;

import hh.fernuni.rentamovie.customer.domain.Customer;

public interface CustomerService {
	Customer createCustomer(String surename, String lastname, LocalDate birthday);

	void updateCustomers(Customer currentUser, String firstname, String lastname, LocalDate birthdate);

	static CustomerService getService() {
		return CustomerServiceImpl.getInstance();
	}
}
