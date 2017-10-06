package hh.fernuni.rentamovie.movie.application;

import java.time.Year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;

public class MovieServiceImpl implements MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final MovieService INSTANCE = new MovieServiceImpl();

	public static MovieService getInstance() {
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
	public void createCopies(Movie movie, int numberToCreate) {
		for (int i = 0; i < numberToCreate; i++) {
			Copy copy = new Copy(movie);
			LOG.info("Created: {}", copy);
		}
	}

	@Override
	public Copy findCopy(Movie movie) {
		return new Copy(movie);
	}

}
