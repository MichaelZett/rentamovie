package hh.fernuni.rentamovie.movie.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class CopyTest {

    @Test
    void shouldCopy() {
        Movie aNewHope = new Movie(Year.of(1977), "A new hope");
        Copy testee = new Copy(aNewHope);

        assertThat(testee.getMovie()).isEqualTo(aNewHope);
    }

    @Test
    void shouldString() {
        Movie aNewHope = new Movie(Year.of(1977), "A new hope");
        Copy testee = new Copy(aNewHope);

        assertThat(testee).hasToString("Copy [id=" + testee.getId() + ", movie=Movie [id=" + aNewHope.getId()
                + ", yearOfPublication=1977, title=A new hope]]");
    }

    @Test
    void shouldId() {
        Movie aNewHope = new Movie(Year.of(1977), "A new hope");
        Copy testee = new Copy(aNewHope);
        Copy testee2 = new Copy(aNewHope);

        assertThat(testee2.getId()).isEqualTo(testee.getId() + 1);
        assertThat(testee.getId()).isLessThanOrEqualTo(testee2.getId());
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
