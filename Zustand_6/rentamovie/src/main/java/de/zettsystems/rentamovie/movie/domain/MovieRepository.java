package de.zettsystems.rentamovie.movie.domain;

import org.jspecify.annotations.Nullable;
import java.util.Collection;

public interface MovieRepository {
    static MovieRepository getRepository() {
        return MovieRepositoryImpl.getInstance();
    }

    void save(Movie movie);

    @Nullable Movie read(Long id);

    Collection<Movie> readAll();
}
