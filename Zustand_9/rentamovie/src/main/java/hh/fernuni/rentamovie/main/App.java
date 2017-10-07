package hh.fernuni.rentamovie.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private Stage primaryStage;
	private BorderPane rootLayout;
	private AnchorPane customerOverview;
	private AnchorPane movieOverview;
	private AnchorPane rentOverview;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("MovieRentApp");
		this.primaryStage.getIcons().add(new Image("images/address_book_32.png"));

		initRootLayout();
		initCustomerOverview();
		initMovieOverview();
		initRentOverview();

		navigateToCustomers();

		// Show the scene containing the root layout.
		Scene scene = new Scene(this.rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("./RootLayout.fxml"));
			this.rootLayout = (BorderPane) loader.load();
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initMovieOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("../movie/adapter/MovieOverview.fxml"));
			this.movieOverview = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initCustomerOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("../customer/adapter/CustomerOverview.fxml"));
			this.customerOverview = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initRentOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("../rent/adapter/RentOverview.fxml"));
			this.rentOverview = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
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
