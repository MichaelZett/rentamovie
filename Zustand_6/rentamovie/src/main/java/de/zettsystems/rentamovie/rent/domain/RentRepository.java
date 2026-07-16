package de.zettsystems.rentamovie.rent.domain;

import java.util.Collection;

public interface RentRepository {
    static RentRepository getRepository() {
        return RentRepositoryImpl.getInstance();
    }

    void save(Rent rent);

    Rent read(Long id);

    Collection<Rent> readAll();
}
