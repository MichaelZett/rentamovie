package hh.fernuni.rentamovie.customer.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Test;

import hh.fernuni.rentamovie.customer.domain.Customer;

public class CustomerServiceImplTest {
	@Test
	public void shouldUpdateCustomer() {
		Customer customer = mock(Customer.class);
		CustomerService testee = CustomerServiceImpl.getInstance();

		testee.updateCustomers(customer, "surename", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("surename", "lastname", LocalDate.of(1983, 3, 22));
	}
}
