package de.zettsystems.rentamovie.movie.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepositoryImpl;

import java.time.Year;

class MovieRepositoryImpl extends CommonRepositoryImpl<Movie> implements MovieRepository {
    private static final MovieRepositoryImpl INSTANCE = new MovieRepositoryImpl(System.getProperty("rentamovie.movie.db", "./movie.db"));

    private MovieRepositoryImpl(String filename) {
        super(filename);
        load();
    }

    @Override
    protected Movie fromText(String[] split) {
        MovieStatus status = split.length > 3 ? MovieStatus.valueOf(split[3]) : MovieStatus.ACTIVE;
        return new Movie(Long.parseLong(split[0]), Year.parse(split[1]), split[2], status);
    }

    @Override
    protected String toText(Movie movie) {
        StringBuilder b = new StringBuilder();
        b.append(movie.getId()).append(DELIMITER);
        b.append(movie.getYearOfPublication().toString()).append(DELIMITER);
        b.append(requireStorableText(movie.getTitle())).append(DELIMITER);
        b.append(movie.getStatus());
        return b.toString();
    }

    static MovieRepository getInstance() {
        return INSTANCE;
    }

}
