package hh.fernuni.rentamovie.common.domain;

public abstract class AbstractIdCarrier implements IdCarrier {
	protected Long id;

	public AbstractIdCarrier() {
		this(IdRepository.getNextId());
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