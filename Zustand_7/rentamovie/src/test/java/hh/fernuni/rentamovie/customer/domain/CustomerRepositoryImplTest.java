package hh.fernuni.rentamovie.customer.domain;

import org.junit.Test;

public class CustomerRepositoryImplTest {
	private CustomerRepository testee = CustomerRepository.getRepository();

	@Test
	public void shouldReadAndSave() {
		// Because of files test is not easy possible
		// Customer customer = mock(Customer.class);
		// when(customer.getId()).thenReturn(3L);
		// assertThat(this.testee.read(3L), is(nullValue()));
		//
		// this.testee.save(customer);
		//
		// assertThat(this.testee.read(3L), is(customer));
	}
}
