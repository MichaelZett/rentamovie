package de.zettsystems.rentamovie.rent.domain;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RentRepositoryImpl implements RentRepository {
    private static final RentRepositoryImpl INSTANCE = new RentRepositoryImpl();
    private final Map<Long, Rent> repo = new ConcurrentHashMap<>();

    private RentRepositoryImpl() {
        // only used in this class
    }

    static RentRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Rent rent) {
        this.repo.put(rent.getId(), rent);
    }

    @Override
    public Rent read(Long id) {
        return this.repo.get(id);
    }

    @Override
    public Collection<Rent> readAll() {
        return this.repo.values();
    }
}
