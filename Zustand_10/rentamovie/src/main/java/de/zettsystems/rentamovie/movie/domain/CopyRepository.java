package de.zettsystems.rentamovie.movie.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepository;

public interface CopyRepository extends CommonRepository<Copy> {

    static CopyRepository getRepository() {
        return CopyRepositoryImpl.getInstance();
    }
}
