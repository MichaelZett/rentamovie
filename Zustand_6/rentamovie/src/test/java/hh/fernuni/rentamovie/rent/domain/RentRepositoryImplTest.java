package hh.fernuni.rentamovie.rent.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentRepositoryImplTest {
    private final RentRepository testee = RentRepository.getRepository();

    @Test
    void shouldReadAndSave() {
        Rent rent = mock(Rent.class);
        when(rent.getId()).thenReturn(400L);

        this.testee.save(rent);

        assertThat(this.testee.read(400L)).isEqualTo(rent);
        assertThat(this.testee.readAll()).contains(rent);
    }
}
