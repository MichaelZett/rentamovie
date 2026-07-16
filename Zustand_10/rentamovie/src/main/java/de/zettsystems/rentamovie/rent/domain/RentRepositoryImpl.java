package de.zettsystems.rentamovie.rent.domain;

import de.zettsystems.rentamovie.common.domain.CommonRepositoryImpl;
import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.customer.domain.CustomerRepository;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.CopyRepository;

import java.time.LocalDate;

class RentRepositoryImpl extends CommonRepositoryImpl<Rent> implements RentRepository {

    private static final RentRepositoryImpl INSTANCE = new RentRepositoryImpl(System.getProperty("rentamovie.rent.db", "./rent.db"));
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
        LocalDate startDate = LocalDate.parse(split[1]);
        LocalDate plannedReturnDate = split.length > 6 ? LocalDate.parse(split[2]) : startDate.plusDays(7);
        int endDateIndex = split.length > 6 ? 3 : 2;
        int customerIndex = split.length > 6 ? 4 : 3;
        int copyIndex = split.length > 6 ? 5 : 4;
        int paymentIndex = split.length > 6 ? 6 : 5;
        Customer customer = this.customerRepository.read(Long.parseLong(split[customerIndex]));
        Copy copy = this.copyRepository.read(Long.parseLong(split[copyIndex]));
        LocalDate endDate = parseEndDate(split[endDateIndex]);
        boolean paid = split.length > paymentIndex && PAID.equals(split[paymentIndex]);
        return new Rent(Long.parseLong(split[0]), startDate, plannedReturnDate, endDate, paid, customer, copy);
    }

    @Override
    protected String toText(Rent domainClass) {
        StringBuilder b = new StringBuilder();
        b.append(domainClass.getId()).append(DELIMITER);
        b.append(domainClass.getStartDate()).append(DELIMITER);
        b.append(domainClass.getPlannedReturnDate()).append(DELIMITER);
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
