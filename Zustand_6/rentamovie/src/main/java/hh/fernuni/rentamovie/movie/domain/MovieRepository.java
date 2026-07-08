package hh.fernuni.rentamovie.movie.domain;

import java.util.Collection;

public interface MovieRepository {
    static MovieRepository getRepository() {
        return MovieRepositoryImpl.getInstance();
    }

    void save(Movie movie);

    Movie read(Long id);

    Collection<Movie> readAll();
}
