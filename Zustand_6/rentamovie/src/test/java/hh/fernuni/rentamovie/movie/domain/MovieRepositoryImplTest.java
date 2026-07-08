package hh.fernuni.rentamovie.movie.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MovieRepositoryImplTest {
    private final MovieRepository testee = MovieRepository.getRepository();

    @Test
    void shouldReadAndSave() {
        Movie movie = mock(Movie.class);
        when(movie.getId()).thenReturn(300L);

        this.testee.save(movie);

        assertThat(this.testee.read(300L)).isEqualTo(movie);
        assertThat(this.testee.readAll()).contains(movie);
    }
}
