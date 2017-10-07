package hh.fernuni.rentamovie.movie.adapter;

import java.time.Year;

import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.application.MovieService;
import hh.fernuni.rentamovie.movie.domain.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MovieOverviewController {
	private Movie currentMovie;
	private MovieService movieService = MovieService.getService();

	@FXML
	private TableView<Movie> movieTable;
	@FXML
	private TableColumn<Customer, Year> yearOfPublicationColumn;
	@FXML
	private TableColumn<Customer, String> titleColumn;
	@FXML
	private TextField yearOfPublicationInput;
	@FXML
	private TextField titleInput;
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
		yearOfPublicationColumn.setCellValueFactory(new PropertyValueFactory<Customer, Year>("yearOfPublication"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("title"));

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

	private void showMovieDetails(Movie movie) {
		if (movie != null) {
			currentMovie = movie;
			yearOfPublicationInput.setText(movie.getYearOfPublication().toString());
			titleInput.setText(movie.getTitle());
			copiesLabel.setText(String.valueOf(movieService.findAllCopiesOfMovie(movie).size()));
		} else {
			clearInput();
		}
	}

	private void clearInput() {
		currentMovie = null;
		yearOfPublicationInput.setText("");
		titleInput.setText("");
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
			movieService.createCopies(currentMovie, 1);
			showMovieDetails(currentMovie);
		}
	}

	@FXML
	private void handleSaveMovie() {
		if (currentMovie != null) {
			movieService.updateMovie(currentMovie, Year.parse(yearOfPublicationInput.getText()), titleInput.getText());
		} else {
			currentMovie = movieService.createMovie(Year.parse(yearOfPublicationInput.getText()), titleInput.getText());
			movieTable.getSelectionModel().select(currentMovie);
		}
		refreshMovies();
	}

}
