package hh.fernuni.rentamovie.movie.domain;

import java.time.Year;

public class Movie {
    private Long id;
    private Year yearOfPublication;
    private String title;
    private MovieStatus status;

    public Movie(Long id, Year yearOfPublication, String title) {
        this(id, yearOfPublication, title, MovieStatus.ACTIVE);
    }

    public Movie(Long id, Year yearOfPublication, String title, MovieStatus status) {
        super();
        this.id = id;
        this.yearOfPublication = yearOfPublication;
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public Year getYearOfPublication() {
        return this.yearOfPublication;
    }

    public String getTitle() {
        return this.title;
    }

    public MovieStatus getStatus() {
        return this.status;
    }

    public boolean isActive() {
        return this.status == MovieStatus.ACTIVE;
    }

    public void updateData(Year yearOfPublication, String title) {
        this.yearOfPublication = yearOfPublication;
        this.title = title;
    }

    public void activate() {
        this.status = MovieStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = MovieStatus.INACTIVE;
    }

    @Override
    public String toString() {
        return "Movie [id=" + this.id + ", yearOfPublication=" + this.yearOfPublication + ", title=" + this.title + "]";
    }

}