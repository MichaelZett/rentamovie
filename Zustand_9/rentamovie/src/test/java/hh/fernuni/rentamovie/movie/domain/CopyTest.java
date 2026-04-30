package hh.fernuni.rentamovie.movie.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Year;

import org.junit.jupiter.api.Test;

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

        assertThat(testee.toString()).isEqualTo("Copy [id=" + testee.getId() + ", movie=Movie [id=" + aNewHope.getId()
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
}
