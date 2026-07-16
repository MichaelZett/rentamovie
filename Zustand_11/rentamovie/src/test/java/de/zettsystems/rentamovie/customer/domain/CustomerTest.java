package de.zettsystems.rentamovie.customer.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void shouldUpdateData() {
        Customer testee = new Customer("firstname", "lastname", LocalDate.of(1983, 3, 22));

        LocalDate newBirthday = LocalDate.of(1982, 4, 23);
        testee.updateData("newFirstname", "newLastname", newBirthday);

        assertThat(testee.getFirstname()).isEqualTo("newFirstname");
        assertThat(testee.getLastname()).isEqualTo("newLastname");
        assertThat(testee.getBirthdate()).isEqualTo(newBirthday);
    }

    @Test
    void shouldChangeStatus() {
        Customer testee = new Customer("firstname", "lastname", LocalDate.of(1983, 3, 22));

        testee.block();

        assertThat(testee.getStatus()).isEqualTo(CustomerStatus.BLOCKED);
        assertThat(testee.isActive()).isFalse();

        testee.activate();

        assertThat(testee.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(testee.isActive()).isTrue();
    }
}
