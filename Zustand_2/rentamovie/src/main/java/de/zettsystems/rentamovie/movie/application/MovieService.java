package de.zettsystems.rentamovie.movie.application;

import java.time.Year;

import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;

public interface MovieService {
	Movie createMovie(Year yearOfPublication, String title);

	void updateMovie(Movie currentMovie, Year year, String title);

	Copy findCopy(Movie movie);
}
