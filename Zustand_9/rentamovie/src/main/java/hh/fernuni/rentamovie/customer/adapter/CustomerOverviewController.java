package hh.fernuni.rentamovie.customer.adapter;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class CustomerOverviewController {
	private Customer currentCustomer;
	private CustomerService customerService = CustomerService.getService();

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
		refreshCustomers();
	}

	private void refreshCustomers() {
		ObservableList<Customer> observableArrayList = FXCollections.observableArrayList();
		observableArrayList.setAll(customerService.readAllCustomers());
		customerTable.setItems(observableArrayList);
		customerTable.refresh();
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
		firstnameInput.setText("");
		lastnameInput.setText("");
		birthdayInput.setText("");
        statusInput.setValue(CustomerStatus.ACTIVE);
		customerTable.getSelectionModel().select(-1);
	}

	@FXML
	private void handleNewCustomer() {
		clearInput();
	}

	@FXML
	private void handleSaveCustomer() {
		if (currentCustomer != null) {
            currentCustomer.updateData(firstnameInput.getText(), lastnameInput.getText(),
					LocalDate.parse(birthdayInput.getText()));
		} else {
			currentCustomer = customerService.createCustomer(firstnameInput.getText(), lastnameInput.getText(),
					LocalDate.parse(birthdayInput.getText()));
			customerTable.getSelectionModel().select(currentCustomer);
		}
        applyStatus(currentCustomer, statusInput.getValue());
        customerService.updateCustomers(currentCustomer, firstnameInput.getText(), lastnameInput.getText(),
                LocalDate.parse(birthdayInput.getText()));
		refreshCustomers();
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

}
