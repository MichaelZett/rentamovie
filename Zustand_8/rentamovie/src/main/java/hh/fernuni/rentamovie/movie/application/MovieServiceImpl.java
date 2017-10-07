package hh.fernuni.rentamovie.movie.application;

import java.time.Year;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.CopyRepository;
import hh.fernuni.rentamovie.movie.domain.Movie;

class MovieServiceImpl implements MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final MovieService INSTANCE = new MovieServiceImpl();
	private CopyRepository copyRepository = CopyRepository.getRepository();

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
	public void createCopies(Movie movie, int numberToCreate) {
		for (int i = 0; i < numberToCreate; i++) {
			Copy copy = new Copy(movie);
			LOG.info("Created: {}", copy);
		}
	}

	@Override
	public Collection<Copy> findAllCopiesOfMovie(Movie movie) {
		return this.copyRepository.readAll().stream().filter(c -> c.getMovie().equals(movie)).collect(Collectors.toSet());
	}

}
