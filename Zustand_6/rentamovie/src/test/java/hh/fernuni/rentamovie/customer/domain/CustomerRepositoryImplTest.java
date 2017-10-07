package hh.fernuni.rentamovie.customer.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CustomerRepositoryImplTest {
	private CustomerRepository testee = CustomerRepository.getRepository();

	@Test
	public void shouldReadAndSave() {
		Customer customer = mock(Customer.class);
		when(customer.getId()).thenReturn(3L);
		assertThat(this.testee.readAll().size(), is(0));
		assertThat(this.testee.read(3L), is(nullValue()));

		this.testee.save(customer);

		assertThat(this.testee.readAll().size(), is(1));
		assertThat(this.testee.read(3L), is(customer));
	}
}
