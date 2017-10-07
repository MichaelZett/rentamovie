package hh.fernuni.rentamovie.customer.domain;

import java.time.LocalDate;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;

public class Customer extends AbstractIdCarrier {
	private String firstname;
	private String lastname;
	private LocalDate birthdate;

	public Customer(String firstname, String lastname, LocalDate birthdate) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
	}

	public void updateData(String firstname, String lastname, LocalDate birthdate) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public LocalDate getBirthdate() {
		return this.birthdate;
	}

	@Override
	public String toString() {
		return "Customer [id=" + this.id + ", firstname=" + this.firstname + ", lastname=" + this.lastname + ", birthdate=" + this.birthdate
		        + "]";
	}

}
