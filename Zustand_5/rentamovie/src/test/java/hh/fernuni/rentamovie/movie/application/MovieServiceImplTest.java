package hh.fernuni.rentamovie.movie.application;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.Movie;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MovieServiceImplTest {

    @Test
    void shouldCreateMovie() {
        MovieService testee = MovieServiceImpl.getInstance();

        Movie movie = testee.createMovie(Year.of(1977), "A new hope");

        assertThat(movie.getYearOfPublication()).isEqualTo(Year.of(1977));
        assertThat(movie.getTitle()).isEqualTo("A new hope");
    }

    @Test
    void shouldUpdateMovie() {
        Movie movie = mock(Movie.class);
        MovieService testee = MovieServiceImpl.getInstance();

        testee.updateMovie(movie, Year.of(1977), "A good hope");

        verify(movie).updateData(Year.of(1977), "A good hope");
    }

    @Test
    void shouldFindCopy() {
        Movie movie = new Movie(Year.of(1977), "A new hope");
        MovieService testee = MovieServiceImpl.getInstance();

        Copy copy = testee.findCopy(movie);

        assertThat(copy.getMovie()).isEqualTo(movie);
    }
}
