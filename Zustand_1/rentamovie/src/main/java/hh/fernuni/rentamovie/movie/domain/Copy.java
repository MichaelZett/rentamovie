package hh.fernuni.rentamovie.movie.domain;

public class Copy {
	private Long id;
	private Movie movie;

	public Copy(Long id, Movie movie) {
		super();
		this.id = id;
		this.movie = movie;
	}

	public Movie getMovie() {
		return this.movie;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Copy [id=" + this.id + ", movie=" + this.movie + "]";
	}

}
