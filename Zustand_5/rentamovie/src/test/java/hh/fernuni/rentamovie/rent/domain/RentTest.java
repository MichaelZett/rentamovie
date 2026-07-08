package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RentTest {

    @Test
    void shouldStartAsOpenRent() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1));

        assertThat(testee.isOpen()).isTrue();
        assertThat(testee.isFinished()).isFalse();
        assertThat(testee.isValid()).isTrue();
    }

    @Test
    void shouldEndRent() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1));

        testee.endRent(LocalDate.of(2026, 7, 4));

        assertThat(testee.getEndDate()).isEqualTo(LocalDate.of(2026, 7, 4));
        assertThat(testee.isOpen()).isFalse();
        assertThat(testee.isFinished()).isTrue();
        assertThat(testee.isValid()).isFalse();
    }
}
