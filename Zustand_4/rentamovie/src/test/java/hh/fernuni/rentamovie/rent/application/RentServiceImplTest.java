package hh.fernuni.rentamovie.rent.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;

public class RentServiceImplTest {
	@Mock
	private MovieService movieServiceMock;

	@InjectMocks
	private RentServiceImpl testee;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldTest() {
		Movie movie = mock(Movie.class);
		Customer customer = mock(Customer.class);
		LocalDate startDate = LocalDate.of(2017, 10, 7);
		Copy copy = mock(Copy.class);
		when(movieServiceMock.findCopy(movie)).thenReturn(copy);

		Rent createdRent = testee.createRent(movie, customer, startDate);

		assertThat(createdRent.getUser(), is(customer));
		assertThat(createdRent.getStartDate(), is(startDate));
		assertThat(createdRent.getCopy(), is(copy));
	}

}
