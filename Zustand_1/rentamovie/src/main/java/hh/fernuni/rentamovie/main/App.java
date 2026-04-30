package hh.fernuni.rentamovie.main;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Year;

import hh.fernuni.rentamovie.movie.domain.Movie;

public class App {
    private static final Logger LOG = System.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOG.log(Level.INFO, "App was started");
        Movie aNewHope = new Movie(1L, Year.of(1977), "A new hope");
        LOG.log(Level.INFO, "{0} was created.", aNewHope);
    }
}
