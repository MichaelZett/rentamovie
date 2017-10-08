package hh.fernuni.rentamovie.customer.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class CustomerTest {

	@Test
	public void shouldUpdateData() throws Exception {
		// Given
		Customer testee = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));
		LocalDate newBirthday = LocalDate.of(1982, 4, 23);

		// When
		testee.updateData("newSurename", "newLastname", newBirthday);

		// Then
		assertThat(testee.getFirstname(), is("newSurename"));
		assertThat(testee.getLastname(), is("newLastname"));
		assertThat(testee.getBirthdate(), is(newBirthday));
	}

	@Test
	public void shouldIncrementId() throws Exception {
		Customer testee = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));
		Customer testee2 = new Customer("surename", "lastname", LocalDate.of(1983, 3, 22));

		assertThat(testee.getId() + 1 == testee2.getId(), is(true));

	}

}
