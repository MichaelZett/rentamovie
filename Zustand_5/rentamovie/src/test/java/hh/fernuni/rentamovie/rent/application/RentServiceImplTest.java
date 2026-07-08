package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldReturnRent() {
        Rent rent = mock(Rent.class);

        testee.returnRent(rent);

        verify(rent).endRent();
    }

    @Test
    void shouldFindOpenRents() {
        Movie movie = mock(Movie.class);
        Customer customer = mock(Customer.class);
        Copy firstCopy = mock(Copy.class);
        Copy secondCopy = mock(Copy.class);
        when(movieServiceMock.findCopy(movie)).thenReturn(firstCopy, secondCopy);
        Rent firstRent = testee.createRent(movie, customer, LocalDate.of(2017, 10, 7));
        Rent secondRent = testee.createRent(movie, customer, LocalDate.of(2017, 10, 8));
        testee.returnRent(firstRent);

        assertThat(testee.findOpenRents()).containsExactly(secondRent);
    }
}
