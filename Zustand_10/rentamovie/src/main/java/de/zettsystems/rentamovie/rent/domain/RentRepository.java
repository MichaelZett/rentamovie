package de.zettsystems.rentamovie.rent.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepository;

public interface RentRepository extends CommonRepository<Rent> {

    static RentRepository getRepository() {
        return RentRepositoryImpl.getInstance();
    }

}
