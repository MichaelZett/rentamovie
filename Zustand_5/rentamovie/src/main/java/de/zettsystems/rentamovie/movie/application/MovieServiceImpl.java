package de.zettsystems.rentamovie.movie.application;

import java.time.Year;

import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;

class MovieServiceImpl implements MovieService {
	private static final MovieService INSTANCE = new MovieServiceImpl();

	private MovieServiceImpl() {
		// should only be called from within this class
	}

	static MovieService getInstance() {
		return INSTANCE;
	}

	@Override
	public Movie createMovie(Year yearOfPublication, String title) {
		return new Movie(yearOfPublication, title);
	}

	@Override
	public void updateMovie(Movie currentMovie, Year year, String title) {
		currentMovie.updateData(year, title);
	}

	@Override
	public Copy findCopy(Movie movie) {
		return new Copy(movie);
	}

}
