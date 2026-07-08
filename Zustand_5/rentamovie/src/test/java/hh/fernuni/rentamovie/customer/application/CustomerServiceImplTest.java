package hh.fernuni.rentamovie.customer.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerServiceImplTest {

	@Test
	void shouldCreateCustomer() {
		CustomerService testee = CustomerServiceImpl.getInstance();

		Customer customer = testee.createCustomer("Luke", "Skywalker", LocalDate.of(1951, 9, 25));

		assertThat(customer.getFirstname()).isEqualTo("Luke");
		assertThat(customer.getLastname()).isEqualTo("Skywalker");
		assertThat(customer.getBirthdate()).isEqualTo(LocalDate.of(1951, 9, 25));
	}

	@Test
	void shouldUpdateCustomer() {
		Customer customer = mock(Customer.class);
		CustomerService testee = CustomerServiceImpl.getInstance();

		testee.updateCustomers(customer, "surename", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("surename", "lastname", LocalDate.of(1983, 3, 22));
	}

	@Test
	void shouldReadEmptyCustomerListBeforeRepositoriesAreIntroduced() {
		CustomerService testee = CustomerServiceImpl.getInstance();

		assertThat(testee.readAllCustomers()).isEmpty();
	}
}
