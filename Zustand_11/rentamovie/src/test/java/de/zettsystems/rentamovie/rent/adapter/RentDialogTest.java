package de.zettsystems.rentamovie.rent.adapter;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class RentDialogTest {

    @Test
    void shouldBuildCustomerDisplayText() {
        Customer customer = new Customer("Jane", "Doe", LocalDate.of(2000, 7, 4));

        assertThat(RentDialog.customerDisplayText(customer)).isEqualTo("Doe, Jane");
    }

    @Test
    void shouldBuildCopyDisplayText() {
        Copy copy = new Copy(new Movie(Year.of(1977), "A new hope"));

        assertThat(RentDialog.copyDisplayText(copy)).isEqualTo("#" + copy.getId() + " - A new hope (1977, DVD)");
    }
}
