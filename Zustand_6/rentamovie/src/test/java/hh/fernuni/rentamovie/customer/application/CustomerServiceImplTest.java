package hh.fernuni.rentamovie.customer.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;

public class CustomerServiceImplTest {
	@Mock
	private CustomerRepository customerRepoMock;

	@InjectMocks
	private CustomerServiceImpl testee;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldUpdateCustomer() {
		Customer customer = mock(Customer.class);

		this.testee.updateCustomers(customer, "surename", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("surename", "lastname", LocalDate.of(1983, 3, 22));
		verify(this.customerRepoMock).save(customer);
	}
}
