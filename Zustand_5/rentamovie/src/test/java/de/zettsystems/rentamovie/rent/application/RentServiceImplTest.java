package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        when(movie.isActive()).thenReturn(true);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(true);
        LocalDate startDate = LocalDate.of(2017, 10, 7);
        Copy copy = mock(Copy.class);
        when(copy.isAvailable()).thenReturn(true);
        when(copy.getMovie()).thenReturn(movie);
        when(movieServiceMock.findCopy(movie)).thenReturn(copy);

        Rent createdRent = testee.createRent(movie, customer, startDate);

        assertThat(createdRent.getCustomer()).isEqualTo(customer);
        assertThat(createdRent.getStartDate()).isEqualTo(startDate);
        assertThat(createdRent.getCopy()).isEqualTo(copy);
    }

    @Test
    void shouldRejectRentForInactiveCustomer() {
        Movie movie = mock(Movie.class);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(false);

        LocalDate date = LocalDate.of(2017, 10, 7);
        assertThatThrownBy(() -> testee.createRent(movie, customer, date))
                .isInstanceOf(IllegalStateException.class);
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
        when(movie.isActive()).thenReturn(true);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(true);
        Copy firstCopy = mock(Copy.class);
        when(firstCopy.isAvailable()).thenReturn(true);
        when(firstCopy.getMovie()).thenReturn(movie);
        Copy secondCopy = mock(Copy.class);
        when(secondCopy.isAvailable()).thenReturn(true);
        when(secondCopy.getMovie()).thenReturn(movie);
        when(movieServiceMock.findCopy(movie)).thenReturn(firstCopy, secondCopy);
        Rent firstRent = testee.createRent(movie, customer, LocalDate.of(2017, 10, 7));
        Rent secondRent = testee.createRent(movie, customer, LocalDate.of(2017, 10, 8));
        testee.returnRent(firstRent);

        assertThat(testee.findOpenRents()).containsExactly(secondRent);
    }
}
