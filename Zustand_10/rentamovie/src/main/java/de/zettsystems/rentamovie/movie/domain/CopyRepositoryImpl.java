package de.zettsystems.rentamovie.movie.domain;

import java.util.Objects;

import de.zettsystems.rentamovie.common.domain.CommonRepositoryImpl;

class CopyRepositoryImpl extends CommonRepositoryImpl<Copy> implements CopyRepository {

    private static final CopyRepositoryImpl INSTANCE = new CopyRepositoryImpl(System.getProperty("rentamovie.copy.db", "./copy.db"));
    private final MovieRepository movieRepository;

    protected CopyRepositoryImpl(String filename) {
        super(filename);
        this.movieRepository = MovieRepository.getRepository();
        load();
    }

    @Override
    protected Copy fromText(String[] split) {
        Movie movie = Objects.requireNonNull(movieRepository.read(Long.parseLong(split[1])), () -> "Unknown movie id: " + split[1]);
        MediaFormat mediaFormat = split.length > 2 ? MediaFormat.valueOf(split[2]) : MediaFormat.DVD;
        CopyStatus status = split.length > 3 ? CopyStatus.valueOf(split[3]) : CopyStatus.AVAILABLE;
        return new Copy(Long.parseLong(split[0]), movie, mediaFormat, status);
    }

    @Override
    protected String toText(Copy domainClass) {
        StringBuilder b = new StringBuilder();
        b.append(domainClass.getId()).append(DELIMITER);
        b.append(domainClass.getMovie().getId()).append(DELIMITER);
        b.append(domainClass.getMediaFormat()).append(DELIMITER);
        b.append(domainClass.getStatus());
        return b.toString();
    }

    static CopyRepository getInstance() {
        return INSTANCE;
    }

}
