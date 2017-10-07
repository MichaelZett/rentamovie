package hh.fernuni.rentamovie.rent.domain;

import java.time.LocalDate;

import hh.fernuni.rentamovie.common.domain.CommonRepositoryImpl;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.CopyRepository;

class RentRepositoryImpl extends CommonRepositoryImpl<Rent> implements RentRepository {

	private static final RentRepositoryImpl INSTANCE = new RentRepositoryImpl("./rent.db");
	private CustomerRepository customerRepository;
	private CopyRepository copyRepository;

	private RentRepositoryImpl(String filename) {
		super(filename);
	}

	@Override
	protected void init() {
		customerRepository = CustomerRepository.getRepository();
		copyRepository = CopyRepository.getRepository();
		super.init();
	}

	@Override
	protected Rent fromText(String[] split) {
		Customer user = customerRepository.read(Long.parseLong(split[3]));
		Copy copy = copyRepository.read(Long.parseLong(split[4]));
		return new Rent(Long.parseLong(split[0]), LocalDate.parse(split[1]), LocalDate.parse(split[2]), user, copy);
	}

	@Override
	protected String toText(Rent domainClass) {
		StringBuilder b = new StringBuilder();
		b.append(domainClass.getId()).append(DELIMITER);
		b.append(domainClass.getStartDate()).append(DELIMITER);
		b.append(domainClass.getEndDate()).append(DELIMITER);
		b.append(domainClass.getUser().getId()).append(DELIMITER);
		b.append(domainClass.getCopy().getId());
		return b.toString();
	}

	static RentRepository getInstance() {
		return INSTANCE;
	}
}
