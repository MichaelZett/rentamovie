package de.zettsystems.rentamovie.rent.adapter;

import de.zettsystems.rentamovie.customer.application.CustomerService;
import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.movie.domain.Copy;
import de.zettsystems.rentamovie.movie.domain.CopyRepository;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

public class RentDialog extends Dialog<Rent> {
    private Label customerLabel = new Label("Customer: ");
    private Label movieLabel = new Label("Movie: ");
    private Label plannedDaysLabel = new Label("Planned days: ");
    private GridPane grid = new GridPane();
    private ComboBox<Customer> customerBox;
    private ComboBox<Copy> movieBox;
    private TextField plannedDaysInput = new TextField("7");

    private CustomerService customerService = CustomerService.getService();
    private CopyRepository copyRepository = CopyRepository.getRepository();
    private RentService rentService = RentService.getService();

    RentDialog() {
        this.setTitle("Input rent");
        ObservableList<Customer> customerOptions = FXCollections.observableArrayList(this.customerService.readActiveCustomers());
        this.customerBox = new ComboBox<>(customerOptions);
        Set<Copy> rentedCopies = this.rentService.findOpenRents().stream()
                .map(Rent::getCopy)
                .collect(Collectors.toSet());
        ObservableList<Copy> movieOptions = FXCollections.observableArrayList(this.copyRepository.readAll().stream()
                .filter(copy -> copy.isAvailable() && copy.getMovie().isActive() && !rentedCopies.contains(copy))
                .toList());
        this.movieBox = new ComboBox<>(movieOptions);

        this.grid.add(this.customerLabel, 1, 1);
        this.grid.add(this.customerBox, 2, 1);
        this.grid.add(this.movieLabel, 1, 2);
        this.grid.add(this.movieBox, 2, 2);
        this.grid.add(this.plannedDaysLabel, 1, 3);
        this.grid.add(this.plannedDaysInput, 2, 3);
        this.getDialogPane().setContent(this.grid);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        this.setResultConverter(b -> {
            if (b != buttonTypeOk || this.movieBox.getValue() == null || this.customerBox.getValue() == null) {
                return null;
            }
            try {
                return this.rentService.createRent(this.movieBox.getValue(), this.customerBox.getValue(),
                        LocalDate.now(ZoneId.systemDefault()), parsePlannedDays());
            } catch (NumberFormatException _) {
                showValidationError("Planned rental duration must be a number between 1 and 7 days.");
                return null;
            } catch (IllegalArgumentException | IllegalStateException e) {
                showValidationError(e.getMessage());
                return null;
            }
        });
    }

    private int parsePlannedDays() {
        int plannedDays = Integer.parseInt(this.plannedDaysInput.getText());
        if (plannedDays < 1 || plannedDays > 7) {
            throw new IllegalArgumentException("Planned rental duration must be between 1 and 7 days.");
        }
        return plannedDays;
    }

    private static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
