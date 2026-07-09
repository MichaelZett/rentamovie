package hh.fernuni.rentamovie.customer.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepoMock;

	@InjectMocks
	private CustomerServiceImpl testee;

	@Test
    void shouldUpdateCustomer() {
		Customer customer = mock(Customer.class);

		this.testee.updateCustomers(customer, "surename", "lastname", LocalDate.of(1983, 3, 22));

		verify(customer).updateData("surename", "lastname", LocalDate.of(1983, 3, 22));
		verify(this.customerRepoMock).save(customer);
	}

    @Test
    void shouldReadActiveCustomers() {
        Customer activeCustomer = mock(Customer.class);
        Customer inactiveCustomer = mock(Customer.class);
        when(activeCustomer.isActive()).thenReturn(true);
        when(inactiveCustomer.isActive()).thenReturn(false);
        when(this.customerRepoMock.readAll()).thenReturn(List.of(activeCustomer, inactiveCustomer));

        assertThat(this.testee.readActiveCustomers()).containsExactly(activeCustomer);
    }
}
