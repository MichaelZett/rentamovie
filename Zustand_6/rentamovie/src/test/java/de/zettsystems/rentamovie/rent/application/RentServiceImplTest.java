package de.zettsystems.rentamovie.rent.application;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;
import de.zettsystems.rentamovie.rent.domain.RentRepository;
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
        LocalDate startDate = LocalDate.of(2026, 7, 1);
        Copy copy = mock(Copy.class);
        when(copy.isAvailable()).thenReturn(true);
        when(copy.getMovie()).thenReturn(movie);
        when(this.movieServiceMock.findCopy(movie)).thenReturn(copy);

        Rent rent = this.testee.createRent(movie, customer, startDate);

        assertThat(rent.getCopy()).isEqualTo(copy);
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldRejectRentForInactiveCustomer() {
        Movie movie = mock(Movie.class);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(false);

        LocalDate date = LocalDate.of(2026, 7, 1);
        assertThatThrownBy(() -> this.testee.createRent(movie, customer, date))
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
    void shouldReadAllRents() {
        Rent rent = mock(Rent.class);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(rent));

        assertThat(this.testee.readAllRents()).containsExactly(rent);
    }

    @Test
    void shouldFindOpenRents() {
        Rent openRent = mock(Rent.class);
        Rent finishedRent = mock(Rent.class);
        when(openRent.isValid()).thenReturn(true);
        when(finishedRent.isValid()).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(openRent, finishedRent));

        assertThat(this.testee.findOpenRents()).containsExactly(openRent);
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
