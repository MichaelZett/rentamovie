package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.rate.application.RateService;
import hh.fernuni.rentamovie.rate.domain.Rate;
import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    @Mock
    private RentRepository rentRepositoryMock;

    @Mock
    private RateService rateServiceMock;

    @InjectMocks
    private RentServiceImpl testee;

    @Test
    void shouldRejectRentForInactiveCustomer() {
        Copy copy = mock(Copy.class);
        Customer customer = mock(Customer.class);
        when(customer.isActive()).thenReturn(false);

        assertThatThrownBy(() -> this.testee.createRent(copy, customer, LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectReturnBeforeStartDate() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 4));

        assertThatThrownBy(() -> this.testee.returnRent(rent, LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectReturningFinishedRent() {
        Rent rent = mock(Rent.class);
        when(rent.isFinished()).thenReturn(true);

        assertThatThrownBy(() -> this.testee.returnRent(rent, LocalDate.of(2026, 7, 4)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldSaveReturnedRent() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 1));

        this.testee.returnRent(rent, LocalDate.of(2026, 7, 4));

        verify(rent).endRent(LocalDate.of(2026, 7, 4));
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldRejectPaymentBeforeReturn() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(true);

        assertThatThrownBy(() -> this.testee.payRent(rent, new BigDecimal("1.00")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectPaymentWithoutAmount() {
        Rent rent = mock(Rent.class);
        when(rent.isOpen()).thenReturn(false);

        assertThatThrownBy(() -> this.testee.payRent(rent, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSavePaidRent() {
        Rent rent = mock(Rent.class);
        Customer customer = customerBornOn(LocalDate.of(1980, 3, 12));
        when(rent.isOpen()).thenReturn(false);
        when(rent.getCustomer()).thenReturn(customer);
        when(rent.getEndDate()).thenReturn(LocalDate.of(2026, 7, 4));
        when(this.rateServiceMock.retrieveRateByAge(46)).thenReturn(Rate.REGULAR);
        when(this.rateServiceMock.calculatePrice(rent, Rate.REGULAR)).thenReturn(new BigDecimal("6.00"));

        this.testee.payRent(rent, new BigDecimal("6.00"));

        verify(rent).markPaid(eq(new BigDecimal("6.00")), any(LocalDate.class));
        verify(this.rentRepositoryMock).save(rent);
    }

    @Test
    void shouldRejectPaymentWithWrongAmount() {
        Rent rent = mock(Rent.class);
        Customer customer = customerBornOn(LocalDate.of(1980, 3, 12));
        when(rent.isOpen()).thenReturn(false);
        when(rent.getCustomer()).thenReturn(customer);
        when(rent.getEndDate()).thenReturn(LocalDate.of(2026, 7, 4));
        when(this.rateServiceMock.retrieveRateByAge(46)).thenReturn(Rate.REGULAR);
        when(this.rateServiceMock.calculatePrice(rent, Rate.REGULAR)).thenReturn(new BigDecimal("6.00"));

        assertThatThrownBy(() -> this.testee.payRent(rent, new BigDecimal("0.01")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldFindPaymentsOnDate() {
        Rent paymentOnDate = mock(Rent.class);
        Rent paymentOnOtherDate = mock(Rent.class);
        Rent openPayment = mock(Rent.class);
        LocalDate date = LocalDate.of(2026, 7, 9);
        when(paymentOnDate.isPaid()).thenReturn(true);
        when(paymentOnDate.getPaymentDate()).thenReturn(date);
        when(paymentOnOtherDate.isPaid()).thenReturn(true);
        when(paymentOnOtherDate.getPaymentDate()).thenReturn(LocalDate.of(2026, 7, 8));
        when(openPayment.isPaid()).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(paymentOnDate, paymentOnOtherDate, openPayment));

        assertThat(this.testee.findPaymentsOn(date)).containsExactly(paymentOnDate);
    }

    @Test
    void shouldSumPaymentsOnDate() {
        Rent firstPayment = mock(Rent.class);
        Rent secondPayment = mock(Rent.class);
        LocalDate date = LocalDate.of(2026, 7, 9);
        when(firstPayment.isPaid()).thenReturn(true);
        when(firstPayment.getPaymentDate()).thenReturn(date);
        when(firstPayment.getPaidAmount()).thenReturn(new BigDecimal("2.50"));
        when(secondPayment.isPaid()).thenReturn(true);
        when(secondPayment.getPaymentDate()).thenReturn(date);
        when(secondPayment.getPaidAmount()).thenReturn(new BigDecimal("3.75"));
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(firstPayment, secondPayment));

        assertThat(this.testee.sumPaymentsOn(date)).isEqualByComparingTo("6.25");
    }

    @Test
    void shouldFindOverdueRents() {
        Rent overdueRent = mock(Rent.class);
        Rent rentInTime = mock(Rent.class);
        LocalDate date = LocalDate.of(2026, 7, 10);
        when(overdueRent.isOverdue(date)).thenReturn(true);
        when(rentInTime.isOverdue(date)).thenReturn(false);
        when(this.rentRepositoryMock.readAll()).thenReturn(List.of(overdueRent, rentInTime));

        assertThat(this.testee.findOverdueRents(date)).containsExactly(overdueRent);
    }

    private static Customer customerBornOn(LocalDate birthdate) {
        Customer customer = mock(Customer.class);
        when(customer.getBirthdate()).thenReturn(birthdate);
        return customer;
    }
}
