package de.zettsystems.rentamovie.movie.adapter;

import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.MediaFormat;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.Locale;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class MovieOverviewController {
    private static final int PAGE_SIZE = 5;

    private Movie currentMovie;
    private MovieService movieService = MovieService.getService();
    private final ObservableList<Movie> movies = FXCollections.observableArrayList();
    private final ObservableList<Movie> pageMovies = FXCollections.observableArrayList();
    private FilteredList<Movie> filteredMovies;
    private SortedList<Movie> sortedMovies;
    private int currentPageIndex;

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
    private Label pageInfoLabel;
    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button previousPageButton;
    @FXML
    private Button nextPageButton;

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
        sortedMovies = new SortedList<>(filteredMovies);
        sortedMovies.comparatorProperty().bind(movieTable.comparatorProperty());
        movieTable.setItems(pageMovies);
        movieTable.comparatorProperty().addListener((observable, oldValue, newValue) -> updatePage(0));
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());

        refreshMovies();
    }

    private void refreshMovies() {
        movies.setAll(movieService.readAllMovies());
        updatePage(0);
    }

    private void applyFilter() {
        String search = searchInput.getText().toLowerCase(Locale.ROOT);
        filteredMovies.setPredicate(movie -> movie.getTitle().toLowerCase(Locale.ROOT).contains(search)
                || movie.getYearOfPublication().toString().contains(search));
        updatePage(0);
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
        movieTable.getSelectionModel().clearSelection();
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
        Year yearOfPublication;
        try {
            yearOfPublication = Year.parse(yearOfPublicationInput.getText());
        } catch (DateTimeParseException _) {
            showValidationError("Invalid year '" + yearOfPublicationInput.getText() + "'. Please use the format 1977.");
            return;
        }
        try {
            if (currentMovie != null) {
                currentMovie.updateData(yearOfPublication, titleInput.getText());
            } else {
                currentMovie = movieService.createMovie(yearOfPublication, titleInput.getText());
                movieTable.getSelectionModel().select(currentMovie);
            }
            applyStatus(currentMovie, statusInput.getValue());
            movieService.updateMovie(currentMovie, yearOfPublication, titleInput.getText());
            refreshMovies();
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        }
    }

    private void updatePage(int pageIndex) {
        int pageCount = Math.max(1, (int) Math.ceil((double) this.sortedMovies.size() / PAGE_SIZE));
        this.currentPageIndex = Math.max(0, Math.min(pageIndex, pageCount - 1));
        int fromIndex = this.currentPageIndex * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, this.sortedMovies.size());
        this.pageMovies.setAll(this.sortedMovies.subList(fromIndex, toIndex));
        this.pageInfoLabel.setText((this.sortedMovies.isEmpty() ? 0 : this.currentPageIndex + 1) + " / " + pageCount);
        this.previousPageButton.setDisable(this.currentPageIndex == 0);
        this.nextPageButton.setDisable(this.currentPageIndex >= pageCount - 1 || this.sortedMovies.isEmpty());
    }

    private static void applyStatus(Movie movie, MovieStatus status) {
        if (status == MovieStatus.INACTIVE) {
            movie.deactivate();
            return;
        }
        movie.activate();
    }

    private static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void handlePreviousPage() {
        updatePage(this.currentPageIndex - 1);
    }

    @FXML
    private void handleNextPage() {
        updatePage(this.currentPageIndex + 1);
    }

}
