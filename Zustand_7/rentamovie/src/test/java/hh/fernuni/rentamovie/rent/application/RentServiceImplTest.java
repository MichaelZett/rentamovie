package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    @Mock
    private MovieService movieServiceMock;

    @Mock
    private RentRepository rentRepositoryMock;

    @InjectMocks
    private RentServiceImpl testee;

    @Test
    void shouldSaveCreatedRent() {
        Movie movie = mock(Movie.class);
        when(movie.isActive()).thenReturn(true);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(true);
        Copy copy = mock(Copy.class);
        when(copy.isAvailable()).thenReturn(true);
        when(copy.getMovie()).thenReturn(movie);
        when(this.movieServiceMock.findAllCopiesOfMovie(movie)).thenReturn(List.of(copy));

        Rent rent = this.testee.createRent(movie, customer, LocalDate.of(2026, 7, 1));

        assertThat(rent.getCopy()).isEqualTo(copy);
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldRejectRentForInactiveCustomer() {
        Movie movie = mock(Movie.class);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(false);

        assertThatThrownBy(() -> this.testee.createRent(movie, customer, LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldSaveReturnedRent() {
        Rent rent = mock(Rent.class);

        this.testee.returnRent(rent);

        verify(rent).endRent();
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldSavePaidRent() {
        Rent rent = mock(Rent.class);

        this.testee.payRent(rent);

        verify(rent).markPaid();
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldFindOpenRents() {
        Rent openRent = mock(Rent.class);
        Rent finishedRent = mock(Rent.class);
        when(openRent.isOpen()).thenReturn(true);
        when(finishedRent.isOpen()).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(openRent, finishedRent));

        assertThat(this.testee.findOpenRents()).containsExactly(openRent);
    }

    @Test
    void shouldFindRentsWithOpenPayment() {
        Rent openPayment = mock(Rent.class);
        Rent paidRent = mock(Rent.class);
        when(openPayment.hasOpenPayment()).thenReturn(true);
        when(paidRent.hasOpenPayment()).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(openPayment, paidRent));

        assertThat(this.testee.findRentsWithOpenPayment()).containsExactly(openPayment);
    }
}
