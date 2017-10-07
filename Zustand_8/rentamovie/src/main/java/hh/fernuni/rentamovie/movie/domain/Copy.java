package hh.fernuni.rentamovie.movie.domain;

import hh.fernuni.rentamovie.common.domain.AbstractIdCarrier;

public class Copy extends AbstractIdCarrier {
	private Movie movie;

	public Copy(Movie movie) {
		super();
		this.movie = movie;
	}

	public Copy(Long id, Movie movie) {
		super(id);
		this.movie = movie;
	}

	public Movie getMovie() {
		return this.movie;
	}

	@Override
	public String toString() {
		return "Copy [id=" + this.id + ", movie=" + this.movie + "]";
	}

}
