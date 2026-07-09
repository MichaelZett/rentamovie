package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

import java.time.LocalDate;
import java.time.ZoneId;

public class Rent {
    private final Long id;
    private final Customer customer;
    private final Copy copy;
    private final LocalDate startDate;
    private final LocalDate plannedReturnDate;
    private LocalDate endDate;

    public Rent(Long id, Customer customer, Copy copy, LocalDate startDate) {
        this(id, customer, copy, startDate, startDate.plusDays(7));
    }

    public Rent(Long id, Customer customer, Copy copy, LocalDate startDate, LocalDate plannedReturnDate) {
        this.id = id;
        this.customer = customer;
        this.copy = copy;
        this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
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

    public LocalDate getPlannedReturnDate() {
        return plannedReturnDate;
    }

    public boolean isValid() {
        return isOpen();
    }

    public boolean isOpen() {
        return endDate == null;
    }

    public boolean isFinished() {
        return !isOpen();
    }

    public boolean isOverdue(LocalDate date) {
        return isOpen() && date.isAfter(plannedReturnDate);
    }

    public void endRent() {
        endRent(LocalDate.now(ZoneId.systemDefault()));
    }

    public void endRent(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Rent [id=" + id + ", customer=" + customer + ", copy=" + copy
                + ", startDate=" + startDate + ", plannedReturnDate=" + plannedReturnDate
                + ", endDate=" + endDate + "]";
    }
}
