package hh.fernuni.rentamovie.rent.domain;

import hh.fernuni.rentamovie.common.domain.CommonRepositoryImpl;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerRepository;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.CopyRepository;

import java.time.LocalDate;

class RentRepositoryImpl extends CommonRepositoryImpl<Rent> implements RentRepository {

    private static final RentRepositoryImpl INSTANCE = new RentRepositoryImpl("./rent.db");
    private static final String OPEN_END_DATE = "OPEN";
    private static final String PAID = "PAID";
    private static final String OPEN_PAYMENT = "OPEN_PAYMENT";
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
        LocalDate endDate = parseEndDate(split[2]);
        boolean paid = split.length > 5 && PAID.equals(split[5]);
        return new Rent(Long.parseLong(split[0]), LocalDate.parse(split[1]), endDate, paid, user, copy);
    }

    @Override
    protected String toText(Rent domainClass) {
        StringBuilder b = new StringBuilder();
        b.append(domainClass.getId()).append(DELIMITER);
        b.append(domainClass.getStartDate()).append(DELIMITER);
        b.append(toText(domainClass.getEndDate())).append(DELIMITER);
        b.append(domainClass.getCustomer().getId()).append(DELIMITER);
        b.append(domainClass.getCopy().getId()).append(DELIMITER);
        b.append(domainClass.isPaid() ? PAID : OPEN_PAYMENT);
        return b.toString();
    }

    private static LocalDate parseEndDate(String endDateAsText) {
        if (OPEN_END_DATE.equals(endDateAsText) || "null".equals(endDateAsText)) {
            return null;
        }
        return LocalDate.parse(endDateAsText);
    }

    private static String toText(LocalDate endDate) {
        if (endDate == null) {
            return OPEN_END_DATE;
        }
        return endDate.toString();
    }

    static RentRepository getInstance() {
        return INSTANCE;
    }
}
