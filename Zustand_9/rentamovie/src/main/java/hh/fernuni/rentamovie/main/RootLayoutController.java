package hh.fernuni.rentamovie.main;

import javafx.fxml.FXML;

public class RootLayoutController {

	// Reference to the main application
	private App mainApp;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	public void navigateToMovies() {
		mainApp.navigateToMovies();
	}

	@FXML
	public void navigateToUsers() {
		mainApp.navigateToCustomers();
	}

	@FXML
	public void navigateToRent() {
		mainApp.navigateToRent();
	}

}