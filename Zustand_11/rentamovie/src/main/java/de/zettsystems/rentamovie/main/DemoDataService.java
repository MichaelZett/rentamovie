package de.zettsystems.rentamovie.main;

import de.zettsystems.rentamovie.customer.application.CustomerService;
import de.zettsystems.rentamovie.customer.domain.CustomerRepository;
import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.CopyRepository;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieRepository;
import de.zettsystems.rentamovie.rent.domain.RentRepository;

import java.time.LocalDate;
import java.time.Year;

public class DemoDataService {
    private final CustomerRepository customerRepository = CustomerRepository.getRepository();
    private final MovieRepository movieRepository = MovieRepository.getRepository();
    private final CopyRepository copyRepository = CopyRepository.getRepository();
    private final RentRepository rentRepository = RentRepository.getRepository();
    private final CustomerService customerService = CustomerService.getService();
    private final MovieService movieService = MovieService.getService();

    public void resetDemoData() {
        this.rentRepository.clear();
        this.copyRepository.clear();
        this.movieRepository.clear();
        this.customerRepository.clear();
        seedDemoData();
    }

    public void seedDemoData() {
        if (!this.customerRepository.readAll().isEmpty() || !this.movieRepository.readAll().isEmpty()) {
            return;
        }
        this.customerService.createCustomer("Luke", "Skywalker", LocalDate.of(1951, 9, 25));
        this.customerService.createCustomer("Leia", "Organa", LocalDate.of(1951, 10, 21));
        this.customerService.createCustomer("Han", "Solo", LocalDate.of(1942, 7, 13));

        Movie aNewHope = this.movieService.createMovie(Year.of(1977), "A new hope");
        Movie empire = this.movieService.createMovie(Year.of(1980), "The empire strikes back");
        Movie returnOfTheJedi = this.movieService.createMovie(Year.of(1983), "Return of the Jedi");
        this.movieService.createCopies(aNewHope, 3);
        this.movieService.createCopies(empire, 2);
        this.movieService.createCopies(returnOfTheJedi, 2);
    }
}
