package de.zettsystems.rentamovie.movie.application;

import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepositoryMock;

    @InjectMocks
    private MovieServiceImpl testee;

    @Test
    void shouldSaveCreatedMovie() {
        Movie movie = this.testee.createMovie(Year.of(1977), "A new hope");

        verify(this.movieRepositoryMock).save(movie);
    }

    @Test
    void shouldSaveUpdatedMovie() {
        Movie movie = mock(Movie.class);

        this.testee.updateMovie(movie, Year.of(1977), "A good hope");

        verify(movie).updateData(Year.of(1977), "A good hope");
        verify(this.movieRepositoryMock).save(movie);
    }

    @Test
    void shouldReadAllMovies() {
        Movie movie = mock(Movie.class);
        when(this.movieRepositoryMock.readAll()).thenReturn(java.util.List.of(movie));

        assertThat(this.testee.readAllMovies()).containsExactly(movie);
    }

    @Test
    void shouldReadActiveMovies() {
        Movie activeMovie = mock(Movie.class);
        Movie inactiveMovie = mock(Movie.class);
        when(activeMovie.isActive()).thenReturn(true);
        when(inactiveMovie.isActive()).thenReturn(false);
        when(this.movieRepositoryMock.readAll()).thenReturn(java.util.List.of(activeMovie, inactiveMovie));

        assertThat(this.testee.readActiveMovies()).containsExactly(activeMovie);
    }
}
