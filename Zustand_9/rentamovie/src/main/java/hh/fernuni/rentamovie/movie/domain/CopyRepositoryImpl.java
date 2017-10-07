package hh.fernuni.rentamovie.movie.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepositoryImpl;

class CopyRepositoryImpl extends CommonRepositoryImpl<Copy> implements CopyRepository {

	private static final CopyRepositoryImpl INSTANCE = new CopyRepositoryImpl("./copy.db");
	private MovieRepository movieRepository;

	protected CopyRepositoryImpl(String filename) {
		super(filename);
	}

	@Override
	protected void init() {
		movieRepository = MovieRepository.getRepository();
		super.init();
	}

	@Override
	protected Copy fromText(String[] split) {
		Movie movie = movieRepository.read(Long.parseLong(split[1]));
		return new Copy(Long.parseLong(split[0]), movie);
	}

	@Override
	protected String toText(Copy domainClass) {
		StringBuilder b = new StringBuilder();
		b.append(domainClass.getId()).append(DELIMITER);
		b.append(domainClass.getMovie().getId());
		return b.toString();
	}

	static CopyRepository getInstance() {
		return INSTANCE;
	}

}
