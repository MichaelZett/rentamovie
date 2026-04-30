package hh.fernuni.rentamovie.rent.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

	@Mock
	private MovieService movieServiceMock;

	@InjectMocks
	private RentServiceImpl testee;

	@Test
    void shouldCreateRent() {
		Movie movie = mock(Movie.class);
		Customer customer = mock(Customer.class);
		LocalDate startDate = LocalDate.of(2017, 10, 7);
		Copy copy = mock(Copy.class);
		when(movieServiceMock.findCopy(movie)).thenReturn(copy);

		Rent createdRent = testee.createRent(movie, customer, startDate);

        assertThat(createdRent.getUser()).isEqualTo(customer);
        assertThat(createdRent.getStartDate()).isEqualTo(startDate);
        assertThat(createdRent.getCopy()).isEqualTo(copy);
	}
}
