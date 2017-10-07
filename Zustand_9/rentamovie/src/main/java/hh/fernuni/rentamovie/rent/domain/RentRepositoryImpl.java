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
		this.customerRepository = CustomerRepository.getRepository();
		this.copyRepository = CopyRepository.getRepository();
		super.init();
	}

	@Override
	protected Rent fromText(String[] split) {
		Customer user = this.customerRepository.read(Long.parseLong(split[3]));
		Copy copy = this.copyRepository.read(Long.parseLong(split[4]));
		LocalDate endDate = split[2].equals("null") ? null : LocalDate.parse(split[2]);
		Rent rent = new Rent(Long.parseLong(split[0]), LocalDate.parse(split[1]), endDate, user, copy);
		return rent;
	}

	@Override
	protected String toText(Rent domainClass) {
		StringBuilder b = new StringBuilder();
		b.append(domainClass.getId()).append(DELIMITER);
		b.append(domainClass.getStartDate()).append(DELIMITER);
		b.append(domainClass.getEndDate()).append(DELIMITER);
		b.append(domainClass.getCustomer().getId()).append(DELIMITER);
		b.append(domainClass.getCopy().getId());
		return b.toString();
	}

	static RentRepository getInstance() {
		return INSTANCE;
	}
}
