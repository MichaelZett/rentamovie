package hh.fernuni.rentamovie.movie.domain;

import java.util.Collection;

public interface MovieRepository {
	public void save(Movie movie);

	public Movie read(Long id);

	public Collection<Movie> readAll();

	static MovieRepository getRepository() {
		return MovieRepositoryImpl.getInstance();
	}

}
