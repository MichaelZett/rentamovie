package hh.fernuni.rentamovie.customer.adapter;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.customer.domain.CustomerStatus;
import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class CustomerOverviewController {
    private static final int PAGE_SIZE = 5;

    private Customer currentCustomer;
    private CustomerService customerService = CustomerService.getService();
    private RentService rentService = RentService.getService();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Customer> pageCustomers = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredCustomers;
    private SortedList<Customer> sortedCustomers;
    private int currentPageIndex;

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
    private TextArea historyArea;
    @FXML
    private Label pageInfoLabel;
    @FXML
    private TextField searchInput;
    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button previousPageButton;
    @FXML
    private Button nextPageButton;

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
        customerTable.setItems(pageCustomers);
        customerTable.comparatorProperty().addListener((observable, oldValue, newValue) -> updatePage(0));
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        refreshCustomers();
    }

    private void refreshCustomers() {
        customers.setAll(customerService.readAllCustomers());
        updatePage(0);
    }

    private void applyFilter() {
        String search = searchInput.getText().toLowerCase(Locale.ROOT);
        filteredCustomers.setPredicate(customer -> customer.getFirstname().toLowerCase(Locale.ROOT).contains(search)
                || customer.getLastname().toLowerCase(Locale.ROOT).contains(search));
        updatePage(0);
    }

    private void showCustomerDetails(Customer user) {
        if (user != null) {
            currentCustomer = user;
            firstnameInput.setText(user.getFirstname());
            lastnameInput.setText(user.getLastname());
            birthdayInput.setText(user.getBirthdate().toString());
            statusInput.setValue(user.getStatus());
            showCustomerHistory(user);
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
        historyArea.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    private void showCustomerHistory(Customer customer) {
        String history = this.rentService.readAllRents().stream()
                .filter(rent -> rent.getCustomer().equals(customer))
                .sorted(Comparator.comparing(Rent::getStartDate).reversed())
                .map(this::formatRentLine)
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .orElse("No rentals yet.");
        this.historyArea.setText(history);
    }

    private String formatRentLine(Rent rent) {
        return rent.getStartDate() + " - " + rent.getCopyTitle() + " - " + rent.getPaymentStatus();
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

    private void updatePage(int pageIndex) {
        int pageCount = Math.max(1, (int) Math.ceil((double) this.sortedCustomers.size() / PAGE_SIZE));
        this.currentPageIndex = Math.max(0, Math.min(pageIndex, pageCount - 1));
        int fromIndex = this.currentPageIndex * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, this.sortedCustomers.size());
        this.pageCustomers.setAll(this.sortedCustomers.subList(fromIndex, toIndex));
        this.pageInfoLabel.setText((this.sortedCustomers.isEmpty() ? 0 : this.currentPageIndex + 1) + " / " + pageCount);
        this.previousPageButton.setDisable(this.currentPageIndex == 0);
        this.nextPageButton.setDisable(this.currentPageIndex >= pageCount - 1 || this.sortedCustomers.isEmpty());
    }

    @FXML
    private void handlePreviousPage() {
        updatePage(this.currentPageIndex - 1);
    }

    @FXML
    private void handleNextPage() {
        updatePage(this.currentPageIndex + 1);
    }

}
