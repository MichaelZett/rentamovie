package hh.fernuni.rentamovie.customer.domain;

import java.time.LocalDate;

/**
 * The Customer.
 * 
 * @author zoelleri
 *
 */
public class Customer {
	private final Long id;
	private String firstname;
	private String lastname;
	private LocalDate birthdate;

	public Customer(Long id, String firstname, String lastname, LocalDate birthdate) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
	}

	public void updateData(String firstname, String lastname, LocalDate birthdate) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthdate = birthdate;
	}

	public Long getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", birthdate=" + birthdate
		        + "]";
	}

}
