package hh.fernuni.rentamovie.movie.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.Year;

import org.junit.Test;

public class CopyTest {
	@Test
	public void shouldCopy() throws Exception {
		Movie aNewHope = new Movie(Year.of(1977), "A new hope");
		Copy testee = new Copy(aNewHope);

		assertThat(aNewHope, is(testee.getMovie()));
	}

	@Test
	public void shouldString() throws Exception {
		Movie aNewHope = new Movie(Year.of(1977), "A new hope");
		Copy testee = new Copy(aNewHope);

		assertThat(testee.toString(), is("Copy [id=" + testee.getId() + ", movie=Movie [id=" + aNewHope.getId()
		        + ", yearOfPublication=1977, title=A new hope]]"));
	}

	@Test
	public void shouldID() throws Exception {
		Movie aNewHope = new Movie(Year.of(1977), "A new hope");
		Copy testee = new Copy(aNewHope);
		Copy testee2 = new Copy(aNewHope);

		assertThat((testee.getId() + 1) == testee2.getId(), is(true));
		assertThat(testee.getId() <= testee2.getId(), is(true));
	}
}
