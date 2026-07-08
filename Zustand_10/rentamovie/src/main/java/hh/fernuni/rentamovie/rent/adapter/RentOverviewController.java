package hh.fernuni.rentamovie.rent.adapter;

import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private final ObservableList<Rent> rents = FXCollections.observableArrayList();
    private FilteredList<Rent> filteredRents;

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
    private TableColumn<Rent, String> paymentStatusColumn;
    @FXML
    private ComboBox<String> statusFilterBox;
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
        this.endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        this.statusFilterBox.setItems(FXCollections.observableArrayList("All", "Open rents", "Open payments", "Paid"));
        this.statusFilterBox.getSelectionModel().select("All");
        this.statusFilterBox.valueProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        this.filteredRents = new FilteredList<>(rents, rent -> true);
        this.rentTable.setItems(filteredRents);
        refreshRents();
    }

    private void refreshRents() {
        this.rents.setAll(this.rentService.readAllRents());
        this.rentTable.refresh();
    }

    private void applyFilter() {
        String selected = this.statusFilterBox.getSelectionModel().getSelectedItem();
        this.filteredRents.setPredicate(rent -> switch (selected) {
            case "Open rents" -> rent.isOpen();
            case "Open payments" -> rent.hasOpenPayment();
            case "Paid" -> rent.isPaid();
            default -> true;
        });
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
            dialog.showAndWait()
                    .map(BigDecimal::new)
                    .ifPresent(amount -> this.rentService.payRent(rent, amount));
            refreshRents();
        }
    }

}
