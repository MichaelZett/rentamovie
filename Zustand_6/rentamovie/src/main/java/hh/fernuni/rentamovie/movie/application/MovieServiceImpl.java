package hh.fernuni.rentamovie.movie.application;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.movie.domain.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.Collection;

class MovieServiceImpl implements MovieService {
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final MovieService INSTANCE = new MovieServiceImpl();
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
    public Collection<Movie> readAllMovies() {
        return this.movieRepository.readAll();
	}

    @Override
    public Collection<Movie> readActiveMovies() {
        return this.movieRepository.readAll().stream()
                .filter(Movie::isActive)
                .toList();
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
