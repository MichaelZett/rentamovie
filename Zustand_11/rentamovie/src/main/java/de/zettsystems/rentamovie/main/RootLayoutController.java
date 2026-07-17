package de.zettsystems.rentamovie.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.List;

public class RootLayoutController {

    private static final String ACTIVE_STYLE_CLASS = "active";

    // Reference to the main application; wired by App right after the FXML load
    @SuppressWarnings("NullAway.Init")
    private App mainApp;

    @FXML
    private Button customersNavButton;
    @FXML
    private Button moviesNavButton;
    @FXML
    private Button rentNavButton;

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
        // App starts on the customer overview
        markActive(this.customersNavButton);
    }

    @FXML
    public void navigateToMovies() {
        markActive(this.moviesNavButton);
        this.mainApp.navigateToMovies();
    }

    @FXML
    public void navigateToCustomers() {
        markActive(this.customersNavButton);
        this.mainApp.navigateToCustomers();
    }

    @FXML
    public void navigateToRent() {
        markActive(this.rentNavButton);
        this.mainApp.navigateToRent();
    }

    @FXML
    public void resetDemoData() {
        // resetting navigates back to the customer overview
        markActive(this.customersNavButton);
        this.mainApp.resetDemoData();
    }

    private void markActive(Button activeButton) {
        for (Button navButton : List.of(this.customersNavButton, this.moviesNavButton, this.rentNavButton)) {
            navButton.getStyleClass().remove(ACTIVE_STYLE_CLASS);
        }
        activeButton.getStyleClass().add(ACTIVE_STYLE_CLASS);
    }

}
