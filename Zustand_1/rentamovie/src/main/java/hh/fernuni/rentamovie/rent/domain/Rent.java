package hh.fernuni.rentamovie.rent.domain;

import java.time.LocalDate;
import java.time.ZoneId;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

public class Rent {
    private final Long id;
    private final Customer customer;
    private final Copy copy;
    private final LocalDate startDate;
    private LocalDate endDate;

    public Rent(Long id, Customer customer, Copy copy, LocalDate startDate) {
        this.id = id;
        this.customer = customer;
        this.copy = copy;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Copy getCopy() {
        return copy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isValid() {
        return endDate == null;
    }

    public void endRent() {
        this.endDate = LocalDate.now(ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return "Rent [id=" + id + ", customer=" + customer + ", copy=" + copy
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
}
