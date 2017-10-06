package hh.fernuni.rentamovie.movie.domain;

import java.time.Year;

public class Movie {
	private Long id;
	private Year yearOfPublication;
	private String title;

	public Movie(Long id, Year yearOfPublication, String title) {
		super();
		this.id = id;
		this.yearOfPublication = yearOfPublication;
		this.title = title;
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

	public void updateData(Year yearOfPublication, String title) {
		this.yearOfPublication = yearOfPublication;
		this.title = title;
	}

	@Override
	public String toString() {
		return "Movie [id=" + this.id + ", yearOfPublication=" + this.yearOfPublication + ", title=" + this.title + "]";
	}

}
