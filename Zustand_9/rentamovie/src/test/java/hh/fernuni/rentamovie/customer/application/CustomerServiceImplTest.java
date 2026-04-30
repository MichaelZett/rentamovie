package hh.fernuni.rentamovie.customer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepoMock;

	@InjectMocks
	private CustomerServiceImpl testee;

	@Test
    void shouldSaveCustomer() {
        this.testee.createCustomer("surename", "lastname", LocalDate.of(1983, 3, 22));

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(this.customerRepoMock).save(captor.capture());
        Customer result = captor.getValue();
        assertThat(result.getFirstname()).isEqualTo("surename");
	}

	@Test
    void shouldUpdateCustomer() {
        Customer customer = mock(Customer.class);

        this.testee.updateCustomers(customer, "surename", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("surename", "lastname", LocalDate.of(1983, 3, 22));
        verify(this.customerRepoMock).save(customer);
	}
}
