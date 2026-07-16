package de.zettsystems.rentamovie.movie.domain;

import de.zettsystems.rentamovie.common.domain.AbstractIdCarrier;

public class Copy extends AbstractIdCarrier {
    private Movie movie;
    private MediaFormat mediaFormat;
    private CopyStatus status;

    public Copy(Movie movie) {
        this(movie, MediaFormat.DVD, CopyStatus.AVAILABLE);
    }

    public Copy(Movie movie, MediaFormat mediaFormat, CopyStatus status) {
        super();
        this.movie = movie;
        this.mediaFormat = mediaFormat;
        this.status = status;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public MediaFormat getMediaFormat() {
        return this.mediaFormat;
    }

    public CopyStatus getStatus() {
        return this.status;
    }

    public boolean isAvailable() {
        return this.status == CopyStatus.AVAILABLE;
    }

    public void markAvailable() {
        this.status = CopyStatus.AVAILABLE;
    }

    public void retire() {
        this.status = CopyStatus.RETIRED;
    }

    public void markLost() {
        this.status = CopyStatus.LOST;
    }

    public void markDamaged() {
        this.status = CopyStatus.DAMAGED;
    }

    @Override
    public String toString() {
        return "Copy [id=" + this.id + ", movie=" + this.movie + "]";
    }

}