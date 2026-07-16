package de.zettsystems.rentamovie.movie.domain;

import org.jspecify.annotations.Nullable;
import java.util.Collection;

public interface MovieRepository {
	public void save(Movie movie);

	public @Nullable Movie read(Long id);

	public Collection<Movie> readAll();

	static MovieRepository getRepository() {
		return MovieRepositoryImpl.getInstance();
	}

}
