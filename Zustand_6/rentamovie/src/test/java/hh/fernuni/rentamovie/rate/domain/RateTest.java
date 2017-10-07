package hh.fernuni.rentamovie.rate.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class RateTest {

	@Test
	public void shouldGetValue() throws Exception {
		assertThat(Rate.JUNIOR.getValue(), is(new BigDecimal("1.00")));

	}
}
