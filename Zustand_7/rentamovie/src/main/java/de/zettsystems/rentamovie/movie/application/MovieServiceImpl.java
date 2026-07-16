package de.zettsystems.rentamovie.movie.application;

import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.CopyRepository;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.Collection;
import java.util.stream.Collectors;

class MovieServiceImpl implements MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final MovieService INSTANCE = new MovieServiceImpl();
	private CopyRepository copyRepository = CopyRepository.getRepository();
	private MovieRepository movieRepository = MovieRepository.getRepository();

	private MovieServiceImpl() {
		// should only be called from within this class
	}

	static MovieService getInstance() {
		return INSTANCE;
	}

	@Override
	public Movie createMovie(Year yearOfPublication, String title) {
		Movie movie = new Movie(yearOfPublication, title);
		this.movieRepository.save(movie);
		return movie;
	}

	@Override
	public void updateMovie(Movie currentMovie, Year year, String title) {
		currentMovie.updateData(year, title);
		this.movieRepository.save(currentMovie);
	}

	@Override
	public void createCopies(Movie movie, int numberToCreate) {
		for (int i = 0; i < numberToCreate; i++) {
			Copy copy = new Copy(movie);
			this.copyRepository.save(copy);
			LOG.info("Created: {}", copy);
		}
	}

	@Override
	public Collection<Copy> findAllCopiesOfMovie(Movie movie) {
		return this.copyRepository.readAll().stream().filter(c -> c.getMovie().equals(movie)).collect(Collectors.toSet());
	}

    @Override
    public Collection<Copy> findAvailableCopiesOfMovie(Movie movie) {
        return findAllCopiesOfMovie(movie).stream()
                .filter(Copy::isAvailable)
                .toList();
    }

	@Override
	public Collection<Movie> readAllMovies() {
		return this.movieRepository.readAll();
	}

    @Override
    public Collection<Movie> readActiveMovies() {
        return this.movieRepository.readAll().stream()
                .filter(Movie::isActive)
                .toList();
    }

}
