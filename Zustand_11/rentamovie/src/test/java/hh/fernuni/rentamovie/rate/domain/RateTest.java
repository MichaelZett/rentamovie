package hh.fernuni.rentamovie.rate.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RateTest {

    @Test
    void shouldGetValue() {
        assertThat(Rate.JUNIOR.getValue()).isEqualTo(new BigDecimal("1.00"));
    }
}
