package hh.fernuni.rentamovie.movie.application;

import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;

class MovieServiceImpl implements MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);

	private static final AtomicLong MOVIE_ID_GENERATOR = new AtomicLong(1L);
	private static final AtomicLong COPY_ID_GENERATOR = new AtomicLong(1L);

	@Override
	public Movie createMovie(Year yearOfPublication, String title) {
		return new Movie(MOVIE_ID_GENERATOR.getAndIncrement(), yearOfPublication, title);
	}

	@Override
	public void updateMovie(Movie currentMovie, Year year, String title) {
		currentMovie.updateData(year, title);
	}

	@Override
	public Copy findCopy(Movie movie) {
		return new Copy(COPY_ID_GENERATOR.getAndIncrement(), movie);
	}

}
