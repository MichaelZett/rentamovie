package hh.fernuni.rentamovie.customer.adapter;

import java.time.LocalDate;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
	private TextField firstnameInput;
	@FXML
	private TextField lastnameInput;
	@FXML
	private TextField birthdayInput;
	@FXML
	private Button newButton;
	@FXML
	private Button saveButton;

	@FXML
	private void initialize() {
		firstnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstname"));
		lastnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastname"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<Customer, LocalDate>("birthdate"));

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
		} else {
			clearInput();
		}
	}

	private void clearInput() {
		firstnameInput.setText("");
		lastnameInput.setText("");
		birthdayInput.setText("");
		customerTable.getSelectionModel().select(-1);
	}

	@FXML
	private void handleNewCustomer() {
		clearInput();
	}

	@FXML
	private void handleSaveCustomer() {
		if (currentCustomer != null) {
			customerService.updateCustomers(currentCustomer, firstnameInput.getText(), lastnameInput.getText(),
					LocalDate.parse(birthdayInput.getText()));
		} else {
			currentCustomer = customerService.createCustomer(firstnameInput.getText(), lastnameInput.getText(),
					LocalDate.parse(birthdayInput.getText()));
			customerTable.getSelectionModel().select(currentCustomer);
		}
		refreshCustomers();
	}

}