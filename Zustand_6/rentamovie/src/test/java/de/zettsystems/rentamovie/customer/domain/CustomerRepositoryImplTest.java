package de.zettsystems.rentamovie.customer.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRepositoryImplTest {

    private final CustomerRepository testee = CustomerRepository.getRepository();

	@Test
    void shouldReadAndSave() {
		Customer customer = mock(Customer.class);
		when(customer.getId()).thenReturn(3L);
        assertThat(this.testee.read(3L)).isNull();

		this.testee.save(customer);

        assertThat(this.testee.readAll()).contains(customer);
        assertThat(this.testee.read(3L)).isEqualTo(customer);
	}
}
