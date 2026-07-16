package de.zettsystems.rentamovie.rent.domain;

import org.jspecify.annotations.Nullable;
import java.util.Collection;

public interface RentRepository {
    static RentRepository getRepository() {
        return RentRepositoryImpl.getInstance();
    }

    void save(Rent rent);

    @Nullable Rent read(Long id);

    Collection<Rent> readAll();
}
