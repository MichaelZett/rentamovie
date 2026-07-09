package hh.fernuni.rentamovie.movie.application;

import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.CopyRepository;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.movie.domain.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private CopyRepository copyRepositoryMock;

    @Mock
    private MovieRepository movieRepositoryMock;

    @InjectMocks
    private MovieServiceImpl testee;

    @Test
    void shouldReadActiveMovies() {
        Movie activeMovie = mock(Movie.class);
        Movie inactiveMovie = mock(Movie.class);
        when(activeMovie.isActive()).thenReturn(true);
        when(inactiveMovie.isActive()).thenReturn(false);
        when(this.movieRepositoryMock.readAll()).thenReturn(List.of(activeMovie, inactiveMovie));

        assertThat(this.testee.readActiveMovies()).containsExactly(activeMovie);
    }

    @Test
    void shouldFindAvailableCopiesOfMovie() {
        Movie movie = mock(Movie.class);
        Copy availableCopy = copyOf(movie, true);
        Copy retiredCopy = copyOf(movie, false);
        Copy otherMovieCopy = copyOfOtherMovie();
        when(this.copyRepositoryMock.readAll()).thenReturn(List.of(availableCopy, retiredCopy, otherMovieCopy));

        assertThat(this.testee.findAvailableCopiesOfMovie(movie)).containsExactly(availableCopy);
    }

    private static Copy copyOf(Movie movie, boolean available) {
        Copy copy = mock(Copy.class);
        when(copy.getMovie()).thenReturn(movie);
        when(copy.isAvailable()).thenReturn(available);
        return copy;
    }

    private static Copy copyOfOtherMovie() {
        Copy copy = mock(Copy.class);
        when(copy.getMovie()).thenReturn(mock(Movie.class));
        return copy;
    }
}
