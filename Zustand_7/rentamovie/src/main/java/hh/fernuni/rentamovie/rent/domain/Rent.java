package hh.fernuni.rentamovie.rent.domain;

import java.time.LocalDate;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

public class Rent extends AbstractIdCarrier {
	private Customer customer;
	private Copy copy;
	private LocalDate startDate;
	private LocalDate endDate;

	public Rent(Customer user, Copy copy, LocalDate startDate) {
		super();
		this.customer = user;
		this.copy = copy;
		this.startDate = startDate;
	}

	public Rent(Long id, LocalDate startDate, LocalDate endDate, Customer user, Copy copy) {
		super(id);
		this.customer = user;
		this.copy = copy;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Customer getUser() {
		return this.customer;
	}

	public Copy getCopy() {
		return this.copy;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public boolean isValid() {
		return this.endDate == null;
	}

	public void endRent() {
		this.endDate = LocalDate.now();
	}

	@Override
	public String toString() {
		return "Rent [id=" + this.id + ", customer=" + this.customer + ", copy=" + this.copy + ", startDate=" + this.startDate
		        + ", endDate=" + this.endDate + "]";
	}

}
