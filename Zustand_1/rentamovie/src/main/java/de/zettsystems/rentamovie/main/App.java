package de.zettsystems.rentamovie.main;

import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.rent.domain.Rent;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;

public class App {
    private static final Logger LOG = System.getLogger(App.class.getName());
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    public static void main(String[] args) {
        LOG.log(Level.INFO, "App was started");
        Movie aNewHope = new Movie(1L, Year.of(1977), "A new hope");
        LOG.log(Level.INFO, "{0} was created.", aNewHope);

        Copy copy = new Copy(1L, aNewHope);
        Customer customer = new Customer(1L, "Luke", "Skywalker", LocalDate.of(1951, 9, 25));
        Rent rent = new Rent(1L, customer, copy, LocalDate.now(SYSTEM_ZONE));

        LOG.log(Level.INFO, "Copy {0} is rented: {1}", copy.getId(), rent.isOpen());

        rent.endRent(LocalDate.now(SYSTEM_ZONE));
        LOG.log(Level.INFO, "Copy {0} is available again: {1}", copy.getId(), rent.isFinished());
    }
}
