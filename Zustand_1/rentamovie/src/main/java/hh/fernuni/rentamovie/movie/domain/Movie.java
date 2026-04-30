package hh.fernuni.rentamovie.movie.domain;

import java.time.Year;

public class Movie {
    private final Long id;
    private Year yearOfPublication;
    private String title;

    public Movie(Long id, Year yearOfPublication, String title) {
        this.id = id;
        this.yearOfPublication = yearOfPublication;
        this.title = title;
    }

    public void updateData(Year yearOfPublication, String title) {
        this.yearOfPublication = yearOfPublication;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public Year getYearOfPublication() {
        return yearOfPublication;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", yearOfPublication=" + yearOfPublication + ", title=" + title + "]";
    }
}
