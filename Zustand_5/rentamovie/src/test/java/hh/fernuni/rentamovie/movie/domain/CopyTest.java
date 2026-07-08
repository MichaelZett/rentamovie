package hh.fernuni.rentamovie.movie.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class CopyTest {

    @Test
    void shouldReferenceMovie() {
        Movie movie = new Movie(Year.of(1977), "A new hope");

        Copy testee = new Copy(movie);

        assertThat(testee.getMovie()).isEqualTo(movie);
    }
}
