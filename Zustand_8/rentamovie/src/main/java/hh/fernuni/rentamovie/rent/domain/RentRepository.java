package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepository;

public interface RentRepository extends CommonRepository<Rent> {

	static RentRepository getRepository() {
		return RentRepositoryImpl.getInstance();
	}

}
