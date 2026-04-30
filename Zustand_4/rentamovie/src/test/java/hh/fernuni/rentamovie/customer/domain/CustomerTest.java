package hh.fernuni.rentamovie.customer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CustomerTest {

	@Test
    void shouldUpdateData() {
		Customer testee = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));
		LocalDate newBirthday = LocalDate.of(1982, 4, 23);

		testee.updateData("newSurename", "newLastname", newBirthday);

        assertThat(testee.getFirstname()).isEqualTo("newSurename");
        assertThat(testee.getLastname()).isEqualTo("newLastname");
        assertThat(testee.getBirthdate()).isEqualTo(newBirthday);
	}

	@Test
    void shouldIncrementId() {
		Customer testee = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));
		Customer testee2 = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));

        assertThat(testee2.getId()).isEqualTo(testee.getId() + 1);
	}
}
