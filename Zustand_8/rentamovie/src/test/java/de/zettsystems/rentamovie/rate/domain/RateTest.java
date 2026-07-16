package de.zettsystems.rentamovie.rate.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class RateTest {

	@Test
    void shouldGetValue() {
        assertThat(Rate.JUNIOR.getValue()).isEqualTo(new BigDecimal("1.00"));
	}
}
