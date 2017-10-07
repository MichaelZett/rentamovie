package hh.fernuni.rentamovie.common.domain;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractIdCarrier implements IdCarrier {
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
	protected Long id;

	public AbstractIdCarrier() {
		this(ID_GENERATOR.getAndIncrement());
	}

	public AbstractIdCarrier(Long id) {
		super();
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
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractIdCarrier other = (AbstractIdCarrier) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

}