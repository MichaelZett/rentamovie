package hh.fernuni.rentamovie.customer.application;

import hh.fernuni.rentamovie.customer.domain.Customer;

import java.time.LocalDate;
import java.util.Collection;

public interface CustomerService {
	Customer createCustomer(String surename, String lastname, LocalDate birthday);

	void updateCustomers(Customer currentUser, String firstname, String lastname, LocalDate birthdate);

    Collection<Customer> readAllCustomers();

    Collection<Customer> readActiveCustomers();

    static CustomerService getService() {
		return CustomerServiceImpl.getInstance();
	}
}
