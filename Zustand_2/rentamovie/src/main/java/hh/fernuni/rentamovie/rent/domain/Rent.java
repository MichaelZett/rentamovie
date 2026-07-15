package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

import java.time.LocalDate;
import java.time.ZoneId;

public class Rent {
	private Long id;
	private Customer customer;
	private Copy copy;
	private LocalDate startDate;
    private LocalDate plannedReturnDate;
	private LocalDate endDate;

	public Rent(Long id, Customer customer, Copy copy, LocalDate startDate) {
        this(id, customer, copy, startDate, startDate.plusDays(7));
    }

    public Rent(Long id, Customer customer, Copy copy, LocalDate startDate, LocalDate plannedReturnDate) {
		super();
		this.id = id;
		this.customer = customer;
		this.copy = copy;
		this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
	}

	public Long getId() {
		return this.id;
	}

	public Customer getCustomer() {
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

    public LocalDate getPlannedReturnDate() {
        return this.plannedReturnDate;
    }

	public boolean isValid() {
		return this.endDate == null;
	}

    public boolean isOverdue(LocalDate date) {
        return this.isValid() && date.isAfter(this.plannedReturnDate);
    }

	public void endRent() {
        this.endDate = LocalDate.now(ZoneId.systemDefault());
	}

	@Override
	public String toString() {
		return "Rent [id=" + this.id + ", customer=" + this.customer + ", copy=" + this.copy + ", startDate=" + this.startDate
                + ", plannedReturnDate=" + this.plannedReturnDate + ", endDate=" + this.endDate + "]";
	}

}
