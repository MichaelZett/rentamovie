package hh.fernuni.rentamovie.rent.adapter;

import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

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
    private TableColumn<Rent, LocalDate> plannedReturnDateColumn;
    @FXML
	private TableColumn<Rent, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Rent, String> paymentStatusColumn;
	@FXML
	private Button newButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button payButton;

	@FXML
	private void initialize() {
        this.customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastname"));
        this.copyColumn.setCellValueFactory(new PropertyValueFactory<>("copyTitle"));
        this.startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.plannedReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedReturnDate"));
        this.endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
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

    @FXML
    private void handleReturnRent() {
        Rent rent = this.rentTable.getSelectionModel().getSelectedItem();
        if (rent != null && rent.isOpen()) {
            this.rentService.returnRent(rent, LocalDate.now(ZoneId.systemDefault()));
            refreshRents();
        }
    }

    @FXML
    private void handlePayRent() {
        Rent rent = this.rentTable.getSelectionModel().getSelectedItem();
        if (rent != null && rent.hasOpenPayment()) {
            TextInputDialog dialog = new TextInputDialog("1.00");
            dialog.setTitle("Pay rent");
            dialog.setHeaderText("Payment amount");
            dialog.setContentText("Amount:");
            dialog.showAndWait().ifPresent(input -> {
                try {
                    this.rentService.payRent(rent, new BigDecimal(input));
                } catch (NumberFormatException _) {
                    showValidationError("Invalid amount '" + input + "'. Please use the format 6.00.");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    showValidationError(e.getMessage());
                }
            });
            refreshRents();
        }
    }

    private static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
