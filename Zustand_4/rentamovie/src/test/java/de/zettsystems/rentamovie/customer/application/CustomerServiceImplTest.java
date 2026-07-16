package de.zettsystems.rentamovie.customer.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.zettsystems.rentamovie.customer.domain.Customer;

class CustomerServiceImplTest {

	@Test
    void shouldUpdateCustomer() {
		Customer customer = mock(Customer.class);
		CustomerService testee = CustomerServiceImpl.getInstance();

		testee.updateCustomer(customer, "firstname", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("firstname", "lastname", LocalDate.of(1983, 3, 22));
	}
}
