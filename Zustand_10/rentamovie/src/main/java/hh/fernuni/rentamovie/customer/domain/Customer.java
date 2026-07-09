package hh.fernuni.rentamovie.customer.domain;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;

import java.time.LocalDate;

public class Customer extends AbstractIdCarrier {
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private CustomerStatus status;

    public Customer(Long id, String firstname, String lastname, LocalDate birthdate) {
        this(id, firstname, lastname, birthdate, CustomerStatus.ACTIVE);
    }

    public Customer(Long id, String firstname, String lastname, LocalDate birthdate, CustomerStatus status) {
        super(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.status = status;
    }

    public Customer(String firstname, String lastname, LocalDate birthdate) {
        this(firstname, lastname, birthdate, CustomerStatus.ACTIVE);
    }

    public Customer(String firstname, String lastname, LocalDate birthdate, CustomerStatus status) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.status = status;
    }

    public void updateData(String firstname, String lastname, LocalDate birthdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public void activate() {
        this.status = CustomerStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = CustomerStatus.INACTIVE;
    }

    public void block() {
        this.status = CustomerStatus.BLOCKED;
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

    public CustomerStatus getStatus() {
        return this.status;
    }

    public boolean isActive() {
        return this.status == CustomerStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "Customer [id=" + this.id + ", firstname=" + this.firstname + ", lastname=" + this.lastname + ", birthdate=" + this.birthdate + "]";
    }

}