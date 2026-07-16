package hh.fernuni.rentamovie.movie.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class MovieTest {

    @Test
    void shouldUpdateData() {
        Movie testee = new Movie(Year.of(1977), "A new hope");

        testee.updateData(Year.of(1980), "The empire strikes back");

        assertThat(testee.getYearOfPublication()).isEqualTo(Year.of(1980));
        assertThat(testee.getTitle()).isEqualTo("The empire strikes back");
    }

    @Test
    void shouldChangeStatus() {
        Movie testee = new Movie(Year.of(1977), "A new hope");

        testee.deactivate();

        assertThat(testee.getStatus()).isEqualTo(MovieStatus.INACTIVE);
        assertThat(testee.isActive()).isFalse();

        testee.activate();

        assertThat(testee.getStatus()).isEqualTo(MovieStatus.ACTIVE);
        assertThat(testee.isActive()).isTrue();
    }
}
