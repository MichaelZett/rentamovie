package hh.fernuni.rentamovie.common.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractIdCarrier implements IdCarrier {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
	protected Long id;

    protected AbstractIdCarrier() {
		this(ID_GENERATOR.getAndIncrement());
	}

    protected AbstractIdCarrier(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "id=" + this.id;
	}

	@Override
	public int hashCode() {
        return Objects.hashCode(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
        if (!(obj instanceof AbstractIdCarrier other)) {
			return false;
		}
        return Objects.equals(this.id, other.id);
	}

}
