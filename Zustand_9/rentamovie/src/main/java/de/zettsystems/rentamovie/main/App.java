package de.zettsystems.rentamovie.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private BorderPane rootLayout;
	private AnchorPane customerOverview;
	private AnchorPane movieOverview;
	private AnchorPane rentOverview;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
    public void start(Stage primaryStage) {
		primaryStage.setTitle("MovieRentApp");
		primaryStage.getIcons().add(new Image("images/address_book_32.png"));

		initRootLayout();
		initCustomerOverview();
		initMovieOverview();
		initRentOverview();

		navigateToCustomers();

		Scene scene = new Scene(this.rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void initRootLayout() {
		try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("RootLayout.fxml"));
            this.rootLayout = loader.load();
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
            LOG.error("Failed to load RootLayout.fxml", e);
		}
	}

	private void initMovieOverview() {
		try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/de/zettsystems/rentamovie/movie/adapter/MovieOverview.fxml"));
            this.movieOverview = loader.load();
		} catch (IOException e) {
            LOG.error("Failed to load MovieOverview.fxml", e);
		}
	}

	private void initCustomerOverview() {
		try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/de/zettsystems/rentamovie/customer/adapter/CustomerOverview.fxml"));
            this.customerOverview = loader.load();
		} catch (IOException e) {
            LOG.error("Failed to load CustomerOverview.fxml", e);
		}
	}

	private void initRentOverview() {
		try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/de/zettsystems/rentamovie/rent/adapter/RentOverview.fxml"));
            this.rentOverview = loader.load();
		} catch (IOException e) {
            LOG.error("Failed to load RentOverview.fxml", e);
		}
	}

	public void navigateToMovies() {
		this.rootLayout.setCenter(this.movieOverview);
	}

	public void navigateToCustomers() {
		this.rootLayout.setCenter(this.customerOverview);
	}

	public void navigateToRent() {
		this.rootLayout.setCenter(this.rentOverview);
	}

}
