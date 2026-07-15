package hh.fernuni.rentamovie.common.domain;

import java.util.Objects;

public abstract class AbstractIdCarrier implements IdCarrier {
    protected Long id;

    protected AbstractIdCarrier() {
        this(IdRepository.getNextId());
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
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        AbstractIdCarrier other = (AbstractIdCarrier) obj;
        return Objects.equals(this.id, other.id);
    }

}
