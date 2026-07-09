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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    void shouldRejectRentWithoutFreeCopy() {
        Movie movie = mock(Movie.class);
        when(movie.isActive()).thenReturn(true);
        when(this.movieServiceMock.findAllCopiesOfMovie(movie)).thenReturn(List.of());

        assertThatThrownBy(() -> this.testee.createRent(movie, activeCustomer(), LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalStateException.class);
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
    void shouldExcludeAlreadyRentedCopy() {
        Movie movie = mock(Movie.class);
        when(movie.isActive()).thenReturn(true);
        Copy copy = mock(Copy.class);
        Rent openRent = mock(Rent.class);
        when(openRent.isOpen()).thenReturn(true);
        when(openRent.getCopy()).thenReturn(copy);
        when(this.movieServiceMock.findAllCopiesOfMovie(movie)).thenReturn(new ArrayList<>(List.of(copy)));
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(openRent));

        assertThat(this.testee.findAllFreeCopies(movie)).isEmpty();
    }

    @Test
    void shouldRejectReturnBeforeStartDate() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 4));

        assertThatThrownBy(() -> this.testee.returnRent(rent, LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSaveReturnedRent() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 1));

        this.testee.returnRent(rent, LocalDate.of(2026, 7, 4));

        verify(rent).endRent(LocalDate.of(2026, 7, 4));
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldRejectPaymentBeforeReturn() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(true);

        assertThatThrownBy(() -> this.testee.payRent(rent, new BigDecimal("1.00")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectPaymentWithoutAmount() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(false);

        assertThatThrownBy(() -> this.testee.payRent(rent, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSavePaidRent() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(false);

        this.testee.payRent(rent, new BigDecimal("1.00"));

        verify(rent).markPaid();
        verify(this.rentRepositoryMock).save(rent);
    }

    private static Customer activeCustomer() {
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(true);
        return customer;
    }

    @Test
    void shouldFindOverdueRents() {
        Rent overdueRent = mock(Rent.class);
        Rent rentInTime = mock(Rent.class);
        LocalDate date = LocalDate.of(2026, 7, 10);
        when(overdueRent.isOverdue(date)).thenReturn(true);
        when(rentInTime.isOverdue(date)).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(overdueRent, rentInTime));

        assertThat(this.testee.findOverdueRents(date)).containsExactly(overdueRent);
    }
}
