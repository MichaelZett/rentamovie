package hh.fernuni.rentamovie.movie.adapter;

import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.MediaFormat;
import hh.fernuni.rentamovie.movie.domain.Movie;
import hh.fernuni.rentamovie.movie.domain.MovieStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.util.Locale;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class MovieOverviewController {
    private Movie currentMovie;
    private MovieService movieService = MovieService.getService();
    private final ObservableList<Movie> movies = FXCollections.observableArrayList();
    private FilteredList<Movie> filteredMovies;

    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, Year> yearOfPublicationColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, MovieStatus> statusColumn;
    @FXML
    private TextField yearOfPublicationInput;
    @FXML
    private TextField titleInput;
    @FXML
    private ComboBox<MovieStatus> statusInput;
    @FXML
    private ComboBox<MediaFormat> mediaFormatInput;
    @FXML
    private TextField searchInput;
    @FXML
    private Label copiesLabel;
    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button copyButton;

    @FXML
    private void initialize() {
        yearOfPublicationColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublication"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusInput.setItems(FXCollections.observableArrayList(MovieStatus.values()));
        mediaFormatInput.setItems(FXCollections.observableArrayList(MediaFormat.values()));
        mediaFormatInput.getSelectionModel().select(MediaFormat.DVD);

        showMovieDetails(null);

        movieTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showMovieDetails(newValue));
        filteredMovies = new FilteredList<>(movies, movie -> true);
        movieTable.setItems(filteredMovies);
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());

        refreshMovies();
    }

    private void refreshMovies() {
        movies.setAll(movieService.readAllMovies());
        movieTable.refresh();
    }

    private void applyFilter() {
        String search = searchInput.getText().toLowerCase(Locale.ROOT);
        filteredMovies.setPredicate(movie -> movie.getTitle().toLowerCase(Locale.ROOT).contains(search)
                || movie.getYearOfPublication().toString().contains(search));
    }

    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            currentMovie = movie;
            yearOfPublicationInput.setText(movie.getYearOfPublication().toString());
            titleInput.setText(movie.getTitle());
            statusInput.setValue(movie.getStatus());
            copiesLabel.setText(String.valueOf(movieService.findAllCopiesOfMovie(movie).size()));
        } else {
            clearInput();
        }
    }

    private void clearInput() {
        currentMovie = null;
        yearOfPublicationInput.setText("");
        titleInput.setText("");
        statusInput.setValue(MovieStatus.ACTIVE);
        mediaFormatInput.getSelectionModel().select(MediaFormat.DVD);
        copiesLabel.setText("");
        movieTable.getSelectionModel().select(-1);
    }

    @FXML
    private void handleNewMovie() {
        clearInput();
    }

    @FXML
    private void handleCopies() {
        if (currentMovie != null) {
            movieService.createCopies(currentMovie, 1, mediaFormatInput.getValue());
            showMovieDetails(currentMovie);
        }
    }

    @FXML
    private void handleSaveMovie() {
        if (currentMovie != null) {
            currentMovie.updateData(Year.parse(yearOfPublicationInput.getText()), titleInput.getText());
        } else {
            currentMovie = movieService.createMovie(Year.parse(yearOfPublicationInput.getText()), titleInput.getText());
            movieTable.getSelectionModel().select(currentMovie);
        }
        applyStatus(currentMovie, statusInput.getValue());
        movieService.updateMovie(currentMovie, Year.parse(yearOfPublicationInput.getText()), titleInput.getText());
        refreshMovies();
    }

    private static void applyStatus(Movie movie, MovieStatus status) {
        if (status == MovieStatus.INACTIVE) {
            movie.deactivate();
            return;
        }
        movie.activate();
    }

}
