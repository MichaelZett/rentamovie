package hh.fernuni.rentamovie.movie.domain;

import java.time.Year;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;

public class Movie extends AbstractIdCarrier {
	private Year yearOfPublication;
	private String title;

	public Movie(Year yearOfPublication, String title) {
		super();
		this.yearOfPublication = yearOfPublication;
		this.title = title;
	}

	@Override
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
