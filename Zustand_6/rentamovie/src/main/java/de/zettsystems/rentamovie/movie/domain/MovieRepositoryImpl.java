package de.zettsystems.rentamovie.movie.domain;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MovieRepositoryImpl implements MovieRepository {
    private static final MovieRepositoryImpl INSTANCE = new MovieRepositoryImpl();
    private final Map<Long, Movie> repo = new ConcurrentHashMap<>();

    private MovieRepositoryImpl() {
        // only used in this class
    }

    static MovieRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Movie movie) {
        this.repo.put(movie.getId(), movie);
    }

    @Override
    public Movie read(Long id) {
        return this.repo.get(id);
    }

    @Override
    public Collection<Movie> readAll() {
        return this.repo.values();
    }
}
