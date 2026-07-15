package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

import java.time.LocalDate;
import java.time.ZoneId;

public class Rent extends AbstractIdCarrier {
    private Customer customer;
    private Copy copy;
    private LocalDate startDate;
    private LocalDate plannedReturnDate;
    private LocalDate endDate;
    private boolean paid;

    public Rent(Customer customer, Copy copy, LocalDate startDate) {
        this(customer, copy, startDate, startDate.plusDays(7));
    }

    public Rent(Customer customer, Copy copy, LocalDate startDate, LocalDate plannedReturnDate) {
        super();
        this.customer = customer;
        this.copy = copy;
        this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
    }

    public Rent(Long id, LocalDate startDate, LocalDate plannedReturnDate, LocalDate endDate, boolean paid, Customer customer, Copy copy) {
        super(id);
        this.customer = customer;
        this.copy = copy;
        this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
        this.endDate = endDate;
        this.paid = paid;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public String getCustomerLastname() {
        return this.customer.getLastname();
    }

    public String getCopyTitle() {
        return this.copy.getMovie().getTitle();
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

    public boolean isOpen() {
        return this.endDate == null;
    }

    public boolean isFinished() {
        return !this.isOpen();
    }

    public boolean isOverdue(LocalDate date) {
        return this.isOpen() && date.isAfter(this.plannedReturnDate);
    }

    public void endRent() {
        this.endRent(LocalDate.now(ZoneId.systemDefault()));
    }

    public void endRent(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public boolean hasOpenPayment() {
        return this.isFinished() && !this.paid;
    }

    public String getPaymentStatus() {
        if (this.isOpen()) {
            return "Open rent";
        }
        if (this.paid) {
            return "Paid";
        }
        return "Open payment";
    }

    public void markPaid() {
        this.paid = true;
    }

    @Override
    public String toString() {
        return "Rent [id=" + this.id + ", customer=" + this.customer + ", copy=" + this.copy + ", startDate=" + this.startDate
                + ", plannedReturnDate=" + this.plannedReturnDate + ", endDate=" + this.endDate + ", paid=" + this.paid + "]";
    }

}
