package hh.fernuni.rentamovie.rate.domain;

import java.math.BigDecimal;

public enum Rate {
	JUNIOR(new BigDecimal("1.00")), REGULAR(new BigDecimal("2.00")), SENIOR(new BigDecimal("1.50"));

	private BigDecimal value;

	private Rate(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

}
