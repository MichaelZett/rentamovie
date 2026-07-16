package de.zettsystems.rentamovie.rent.domain;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RentTest {

    @Test
    void shouldTrackOpenPaymentAfterReturn() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1));

        testee.endRent(LocalDate.of(2026, 7, 4));

        assertThat(testee.isFinished()).isTrue();
        assertThat(testee.hasOpenPayment()).isTrue();
        assertThat(testee.getPaymentStatus()).isEqualTo("Open payment");
    }

    @Test
    void shouldMarkRentAsPaid() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1));
        testee.endRent(LocalDate.of(2026, 7, 4));

        testee.markPaid();

        assertThat(testee.isPaid()).isTrue();
        assertThat(testee.hasOpenPayment()).isFalse();
        assertThat(testee.getPaymentStatus()).isEqualTo("Paid");
    }

    @Test
    void shouldUseDefaultPlannedReturnDate() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1));

        assertThat(testee.getPlannedReturnDate()).isEqualTo(LocalDate.of(2026, 7, 8));
    }

    @Test
    void shouldDetectOverdueOpenRent() {
        Rent testee = new Rent(mock(Customer.class), mock(Copy.class), LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3));

        assertThat(testee.isOverdue(LocalDate.of(2026, 7, 4))).isTrue();
        assertThat(testee.isOverdue(LocalDate.of(2026, 7, 3))).isFalse();
    }
}
