package hh.fernuni.rentamovie.movie.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepository;

public interface CopyRepository extends CommonRepository<Copy> {

	static CopyRepository getRepository() {
		return CopyRepositoryImpl.getInstance();
	}
}
