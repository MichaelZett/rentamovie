package hh.fernuni.rentamovie.movie.application;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;

import java.time.Year;
import java.util.Collection;

public interface MovieService {
	Movie createMovie(Year yearOfPublication, String title);

	void createCopies(Movie movie, int count);

	void updateMovie(Movie currentMovie, Year year, String title);

	Collection<Movie> readAllMovies();

    Collection<Movie> readActiveMovies();

	static MovieService getService() {
		return MovieServiceImpl.getInstance();
	}

	Collection<Copy> findAllCopiesOfMovie(Movie movie);

    Collection<Copy> findAvailableCopiesOfMovie(Movie movie);

}
