package de.zettsystems.rentamovie.common.domain;

import org.jspecify.annotations.Nullable;
import java.util.Collection;

public interface CommonRepository<T extends AbstractIdCarrier> {
	public void save(T entity);

	public @Nullable T read(Long id);

	public Collection<T> readAll();

}
