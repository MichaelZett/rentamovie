package hh.fernuni.rentamovie.common.domain;

import java.util.Collection;

public interface CommonRepository<T extends AbstractIdCarrier> {
	public void save(T entity);

	public T read(Long id);

	public Collection<T> readAll();

}
