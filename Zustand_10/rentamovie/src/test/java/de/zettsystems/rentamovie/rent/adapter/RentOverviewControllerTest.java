package de.zettsystems.rentamovie.rent.adapter;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

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
}
