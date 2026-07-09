package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

public class Rent extends AbstractIdCarrier {
    private Customer customer;
    private Copy copy;
    private LocalDate startDate;
    private LocalDate plannedReturnDate;
    private LocalDate endDate;
    private boolean paid;
    private LocalDate paymentDate;
    private BigDecimal paidAmount = BigDecimal.ZERO;

    public Rent(Customer user, Copy copy, LocalDate startDate) {
        this(user, copy, startDate, startDate.plusDays(7));
    }

    public Rent(Customer user, Copy copy, LocalDate startDate, LocalDate plannedReturnDate) {
        super();
        this.customer = user;
        this.copy = copy;
        this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
    }

    public Rent(Long id, LocalDate startDate, LocalDate endDate, boolean paid, Customer user, Copy copy) {
        this(id, startDate, startDate.plusDays(7), endDate, paid, user, copy);
    }

    public Rent(Long id, LocalDate startDate, LocalDate plannedReturnDate, LocalDate endDate, boolean paid, Customer user, Copy copy) {
        this(id, startDate, plannedReturnDate, endDate, paid, null, BigDecimal.ZERO, user, copy);
    }

    public Rent(Long id, LocalDate startDate, LocalDate plannedReturnDate, LocalDate endDate, boolean paid,
                LocalDate paymentDate, BigDecimal paidAmount, Customer user, Copy copy) {
        super(id);
        this.customer = user;
        this.copy = copy;
        this.startDate = startDate;
        this.plannedReturnDate = plannedReturnDate;
        this.endDate = endDate;
        this.paid = paid;
        this.paymentDate = paymentDate;
        this.paidAmount = paidAmount;
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

    public boolean isValid() {
        return this.isOpen();
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

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public BigDecimal getPaidAmount() {
        return this.paidAmount;
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
        this.markPaid(BigDecimal.ZERO, LocalDate.now(ZoneId.systemDefault()));
    }

    public void markPaid(BigDecimal amount, LocalDate paymentDate) {
        this.paid = true;
        this.paidAmount = amount;
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Rent [id=" + this.id + ", customer=" + this.customer + ", copy=" + this.copy
                + ", startDate=" + this.startDate + ", plannedReturnDate=" + this.plannedReturnDate
                + ", endDate=" + this.endDate + ", paid=" + this.paid + ", paymentDate=" + this.paymentDate
                + ", paidAmount=" + this.paidAmount + "]";
    }

}
