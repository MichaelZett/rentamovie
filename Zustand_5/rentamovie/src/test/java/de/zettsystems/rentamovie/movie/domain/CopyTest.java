package de.zettsystems.rentamovie.movie.domain;

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

    @Test
    void shouldTrackFormatAndStatus() {
        Movie aNewHope = new Movie(Year.of(1977), "A new hope");
        Copy testee = new Copy(aNewHope, MediaFormat.BLU_RAY, CopyStatus.AVAILABLE);

        testee.retire();

        assertThat(testee.getMediaFormat()).isEqualTo(MediaFormat.BLU_RAY);
        assertThat(testee.getStatus()).isEqualTo(CopyStatus.RETIRED);
        assertThat(testee.isAvailable()).isFalse();
    }
}
