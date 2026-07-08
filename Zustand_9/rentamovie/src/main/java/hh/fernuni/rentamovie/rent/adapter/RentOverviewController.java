package hh.fernuni.rentamovie.rent.adapter;

import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class RentOverviewController {
	private RentService rentService = RentService.getService();

	@FXML
	private TableView<Rent> rentTable;
	@FXML
	private TableColumn<Rent, String> customerColumn;
	@FXML
	private TableColumn<Rent, String> copyColumn;
	@FXML
	private TableColumn<Rent, LocalDate> startDateColumn;
	@FXML
	private TableColumn<Rent, LocalDate> endDateColumn;
	@FXML
	private Button newButton;

	@FXML
	private void initialize() {
        this.customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastname"));
        this.copyColumn.setCellValueFactory(new PropertyValueFactory<>("copyTitle"));
        this.startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		refreshRents();
	}

	private void refreshRents() {
		ObservableList<Rent> observableArrayList = FXCollections.observableArrayList();
		observableArrayList.setAll(this.rentService.readAllRents());
		this.rentTable.setItems(observableArrayList);
		this.rentTable.refresh();
	}

	@FXML
	private void handleNewRent() {
		Dialog<Rent> dialog = new RentDialog();
        dialog.showAndWait();
		refreshRents();
	}

}