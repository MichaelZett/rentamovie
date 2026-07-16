package de.zettsystems.rentamovie.movie.application;

import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieRepository;

import java.time.Year;
import java.util.Collection;

class MovieServiceImpl implements MovieService {
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
	public Copy findCopy(Movie movie) {
		return new Copy(movie);
	}

}
