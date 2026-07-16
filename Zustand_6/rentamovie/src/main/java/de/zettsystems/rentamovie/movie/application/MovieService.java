package de.zettsystems.rentamovie.movie.application;

import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;

import java.time.Year;
import java.util.Collection;

public interface MovieService {
	Movie createMovie(Year yearOfPublication, String title);

	void updateMovie(Movie currentMovie, Year year, String title);

	Collection<Movie> readAllMovies();

    Collection<Movie> readActiveMovies();

	Copy findCopy(Movie movie);

	static MovieService getService() {
		return MovieServiceImpl.getInstance();
	}
}
