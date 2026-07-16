package de.zettsystems.rentamovie.customer.adapter;

import de.zettsystems.rentamovie.customer.application.CustomerService;
import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerOverviewControllerTest {

    @Test
    void shouldBuildCustomerHistoryText() {
        RentService rentService = mock(RentService.class);
        CustomerOverviewController testee = new CustomerOverviewController(CustomerService.getService(), rentService);
        Movie movie = new Movie(Year.of(2026), "Demo");
        Copy copy = new Copy(movie);
        Customer customer = new Customer("Jane", "Doe", LocalDate.of(2000, 7, 4));
        Customer otherCustomer = new Customer("John", "Smith", LocalDate.of(1990, 1, 1));
        Rent olderRent = new Rent(customer, copy, LocalDate.of(2026, 6, 1));
        olderRent.endRent(LocalDate.of(2026, 6, 4));
        Rent newerRent = new Rent(customer, copy, LocalDate.of(2026, 7, 1));
        Rent otherRent = new Rent(otherCustomer, copy, LocalDate.of(2026, 5, 1));
        when(rentService.readAllRents()).thenReturn(List.of(olderRent, newerRent, otherRent));

        String history = testee.customerHistoryText(customer);

        assertThat(history.lines())
                .containsExactly("2026-07-01 - Demo - Open rent", "2026-06-01 - Demo - Open payment");
    }

    @Test
    void shouldShowPlaceholderForCustomerWithoutRents() {
        RentService rentService = mock(RentService.class);
        CustomerOverviewController testee = new CustomerOverviewController(CustomerService.getService(), rentService);
        Customer customer = new Customer("Jane", "Doe", LocalDate.of(2000, 7, 4));
        when(rentService.readAllRents()).thenReturn(List.of());

        assertThat(testee.customerHistoryText(customer)).isEqualTo("No rentals yet.");
    }

    @Test
    void shouldCalculatePageCount() {
        assertThat(CustomerOverviewController.pageCount(0)).isEqualTo(1);
        assertThat(CustomerOverviewController.pageCount(5)).isEqualTo(1);
        assertThat(CustomerOverviewController.pageCount(6)).isEqualTo(2);
    }

    @Test
    void shouldClampPageIndex() {
        assertThat(CustomerOverviewController.clampPageIndex(-1, 3)).isEqualTo(0);
        assertThat(CustomerOverviewController.clampPageIndex(1, 3)).isEqualTo(1);
        assertThat(CustomerOverviewController.clampPageIndex(7, 3)).isEqualTo(2);
    }
}
