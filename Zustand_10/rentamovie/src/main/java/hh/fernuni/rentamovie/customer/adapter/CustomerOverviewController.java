package hh.fernuni.rentamovie.customer.adapter;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class CustomerOverviewController {
    private Customer currentCustomer;
    private CustomerService customerService = CustomerService.getService();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredCustomers;
    private SortedList<Customer> sortedCustomers;

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> firstnameColumn;
    @FXML
    private TableColumn<Customer, String> lastnameColumn;
    @FXML
    private TableColumn<Customer, LocalDate> birthdayColumn;
    @FXML
    private TableColumn<Customer, CustomerStatus> statusColumn;
    @FXML
    private TextField firstnameInput;
    @FXML
    private TextField lastnameInput;
    @FXML
    private TextField birthdayInput;
    @FXML
    private ComboBox<CustomerStatus> statusInput;
    @FXML
    private TextField searchInput;
    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusInput.setItems(FXCollections.observableArrayList(CustomerStatus.values()));

        showCustomerDetails(null);

        customerTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showCustomerDetails(newValue));
        filteredCustomers = new FilteredList<>(customers, customer -> true);
        sortedCustomers = new SortedList<>(filteredCustomers);
        sortedCustomers.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedCustomers);
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        refreshCustomers();
    }

    private void refreshCustomers() {
        customers.setAll(customerService.readAllCustomers());
        customerTable.refresh();
    }

    private void applyFilter() {
        String search = searchInput.getText().toLowerCase(Locale.ROOT);
        filteredCustomers.setPredicate(customer -> customer.getFirstname().toLowerCase(Locale.ROOT).contains(search)
                || customer.getLastname().toLowerCase(Locale.ROOT).contains(search));
    }

    private void showCustomerDetails(Customer user) {
        if (user != null) {
            currentCustomer = user;
            firstnameInput.setText(user.getFirstname());
            lastnameInput.setText(user.getLastname());
            birthdayInput.setText(user.getBirthdate().toString());
            statusInput.setValue(user.getStatus());
        } else {
            clearInput();
        }
    }

    private void clearInput() {
        currentCustomer = null;
        firstnameInput.setText("");
        lastnameInput.setText("");
        birthdayInput.setText("");
        statusInput.setValue(CustomerStatus.ACTIVE);
        customerTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleNewCustomer() {
        clearInput();
    }

    @FXML
    private void handleSaveCustomer() {
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(birthdayInput.getText());
        } catch (DateTimeParseException _) {
            showValidationError("Invalid birthday '" + birthdayInput.getText() + "'. Please use the format 2001-12-24.");
            return;
        }
        try {
            if (currentCustomer != null) {
                currentCustomer.updateData(firstnameInput.getText(), lastnameInput.getText(), birthdate);
            } else {
                currentCustomer = customerService.createCustomer(firstnameInput.getText(), lastnameInput.getText(), birthdate);
                customerTable.getSelectionModel().select(currentCustomer);
            }
            applyStatus(currentCustomer, statusInput.getValue());
            customerService.updateCustomers(currentCustomer, firstnameInput.getText(), lastnameInput.getText(), birthdate);
            refreshCustomers();
        } catch (IllegalArgumentException e) {
            showValidationError(e.getMessage());
        }
    }

    private static void applyStatus(Customer customer, CustomerStatus status) {
        if (status == CustomerStatus.INACTIVE) {
            customer.deactivate();
            return;
        }
        if (status == CustomerStatus.BLOCKED) {
            customer.block();
            return;
        }
        customer.activate();
    }

    private static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
