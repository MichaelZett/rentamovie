package hh.fernuni.rentamovie.main;

import java.time.Year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Movie;

public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		LOG.info("App was started");
		MovieService movieService = MovieService.getService();
		Movie aNewHope = movieService.createMovie(Year.of(1977), "A new hope");
		LOG.info("{} was created.", aNewHope);
	}

}
