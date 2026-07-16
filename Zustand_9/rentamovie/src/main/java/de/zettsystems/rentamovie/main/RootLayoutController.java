package de.zettsystems.rentamovie.main;

import javafx.fxml.FXML;

public class RootLayoutController {

	// Reference to the main application; wired by App right after the FXML load
	@SuppressWarnings("NullAway.Init")
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