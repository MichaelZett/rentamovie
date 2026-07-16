package de.zettsystems.rentamovie.rent.adapter;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rate.application.RateService;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentOverviewControllerTest {

    @Test
    void shouldCalculateExpectedPaymentAmountFromRent() {
        RentOverviewController testee = new RentOverviewController();
        Movie movie = new Movie(Year.of(2026), "Demo");
        Copy copy = new Copy(movie);
        Customer customer = new Customer("Jane", "Doe", LocalDate.of(2000, 7, 4));
        Rent rent = new Rent(customer, copy, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 4));
        rent.endRent(LocalDate.of(2026, 7, 4));

        BigDecimal expectedAmount = testee.expectedPaymentAmount(rent);

        assertThat(expectedAmount).isEqualByComparingTo("6.00");
    }

    @Test
    void shouldBuildDailyClosingText() {
        RentService rentService = mock(RentService.class);
        RentOverviewController testee = new RentOverviewController(rentService, RateService.getService());
        Movie movie = new Movie(Year.of(2026), "Demo");
        Copy copy = new Copy(movie);
        Customer customer = new Customer("Jane", "Doe", LocalDate.of(2000, 7, 4));
        Rent rent = new Rent(customer, copy, LocalDate.of(2026, 7, 1));
        rent.endRent(LocalDate.of(2026, 7, 4));
        rent.markPaid(new BigDecimal("6.00"), LocalDate.of(2026, 7, 9));
        when(rentService.findPaymentsOn(LocalDate.of(2026, 7, 9))).thenReturn(List.of(rent));
        when(rentService.sumPaymentsOn(LocalDate.of(2026, 7, 9))).thenReturn(new BigDecimal("6.00"));

        String dailyClosingText = testee.dailyClosingText(LocalDate.of(2026, 7, 9));

        assertThat(dailyClosingText)
                .contains("Doe - Demo - 6.00")
                .contains("Total: 6.00");
    }
}
