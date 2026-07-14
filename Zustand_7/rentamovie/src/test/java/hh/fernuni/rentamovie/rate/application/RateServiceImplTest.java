package hh.fernuni.rentamovie.rate.application;

import hh.fernuni.rentamovie.rate.domain.Rate;
import hh.fernuni.rentamovie.rent.domain.Rent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RateServiceImplTest {

    @Test
    void shouldCalculatePrice() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(rent.getEndDate()).thenReturn(LocalDate.of(2026, 7, 4));
        RateService testee = RateServiceImpl.getInstance();

        assertThat(testee.calculatePrice(rent, Rate.REGULAR)).isEqualByComparingTo(new BigDecimal("6.00"));
    }

    @Test
    void shouldRejectPriceForOpenRent() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(true);
        RateService testee = RateServiceImpl.getInstance();

        assertThatThrownBy(() -> testee.calculatePrice(rent, Rate.REGULAR))
                .isInstanceOf(IllegalStateException.class);
    }
}
