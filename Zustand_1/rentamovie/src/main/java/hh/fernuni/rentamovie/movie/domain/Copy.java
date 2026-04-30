package hh.fernuni.rentamovie.movie.domain;

public class Copy {
    private final Long id;
    private final Movie movie;

    public Copy(Long id, Movie movie) {
        this.id = id;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "Copy [id=" + id + ", movie=" + movie + "]";
    }
}
