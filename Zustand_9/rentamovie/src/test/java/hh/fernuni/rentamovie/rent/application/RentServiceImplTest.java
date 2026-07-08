package hh.fernuni.rentamovie.rent.application;

import hh.fernuni.rentamovie.rent.domain.Rent;
import hh.fernuni.rentamovie.rent.domain.RentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    @Mock
    private RentRepository rentRepositoryMock;

    @InjectMocks
    private RentServiceImpl testee;

    @Test
    void shouldRejectReturnBeforeStartDate() {
        Rent rent = mock(Rent.class);
        when(rent.getStartDate()).thenReturn(LocalDate.of(2026, 7, 4));

        assertThatThrownBy(() -> this.testee.returnRent(rent, LocalDate.of(2026, 7, 1)))
                .isInstanceOf(IllegalArgumentException.class);
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
        when(rent.isOpen()).thenReturn(false);

        this.testee.payRent(rent, new BigDecimal("1.00"));

        verify(rent).markPaid();
        verify(this.rentRepositoryMock).save(rent);
    }
}
