package hh.fernuni.rentamovie.movie.application;

import java.time.Year;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;

public interface MovieService {
	Movie createMovie(Year yearOfPublication, String title);

	void createCopies(Movie movie, int count);

	void updateMovie(Movie currentMovie, Year year, String title);

	Copy findCopy(Movie movie);

	static MovieService getService() {
		return MovieServiceImpl.getInstance();
	}
}
