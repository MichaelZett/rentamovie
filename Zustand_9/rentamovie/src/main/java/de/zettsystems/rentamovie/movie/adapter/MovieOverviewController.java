package de.zettsystems.rentamovie.movie.adapter;

import de.zettsystems.rentamovie.movie.application.MovieService;
import de.zettsystems.rentamovie.movie.domain.MediaFormat;
import de.zettsystems.rentamovie.movie.domain.Movie;
import de.zettsystems.rentamovie.movie.domain.MovieStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import de.zettsystems.rentamovie.common.adapter.Theme;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.jspecify.annotations.Nullable;
import java.time.Year;
import java.time.format.DateTimeParseException;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class MovieOverviewController {
	private @Nullable Movie currentMovie;
	private MovieService movieService = MovieService.getService();

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

		refreshMovies();
	}

	private void refreshMovies() {
		ObservableList<Movie> observableArrayList = FXCollections.observableArrayList();
		observableArrayList.setAll(movieService.readAllMovies());
		movieTable.setItems(observableArrayList);
		movieTable.refresh();
	}

	private void showMovieDetails(@Nullable Movie movie) {
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

    private static void applyStatus(Movie movie, MovieStatus status) {
        if (status == MovieStatus.INACTIVE) {
            movie.deactivate();
            return;
        }
        movie.activate();
    }

    private static void showValidationError(@Nullable String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        Theme.apply(alert.getDialogPane());
        alert.showAndWait();
    }

}
