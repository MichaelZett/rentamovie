package de.zettsystems.rentamovie.customer.adapter;

import de.zettsystems.rentamovie.customer.application.CustomerService;
import de.zettsystems.rentamovie.customer.domain.Customer;
import de.zettsystems.rentamovie.customer.domain.CustomerStatus;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import de.zettsystems.rentamovie.common.adapter.Theme;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.jspecify.annotations.Nullable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class CustomerOverviewController {
    private static final int PAGE_SIZE = 5;

    private @Nullable Customer currentCustomer;
    private CustomerService customerService;
    private RentService rentService;
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Customer> pageCustomers = FXCollections.observableArrayList();
    private final FilteredList<Customer> filteredCustomers = new FilteredList<>(customers, customer -> true);
    private final SortedList<Customer> sortedCustomers = new SortedList<>(filteredCustomers);
    private int currentPageIndex;

    public CustomerOverviewController() {
        this(CustomerService.getService(), RentService.getService());
    }

    CustomerOverviewController(CustomerService customerService, RentService rentService) {
        this.customerService = customerService;
        this.rentService = rentService;
    }

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
    private DatePicker birthdayInput;
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

    private void showCustomerDetails(@Nullable Customer customer) {
        if (customer != null) {
            currentCustomer = customer;
            firstnameInput.setText(customer.getFirstname());
            lastnameInput.setText(customer.getLastname());
            birthdayInput.setValue(customer.getBirthdate());
            statusInput.setValue(customer.getStatus());
            showCustomerHistory(customer);
        } else {
            clearInput();
        }
    }

    private void clearInput() {
        currentCustomer = null;
        firstnameInput.setText("");
        lastnameInput.setText("");
        birthdayInput.setValue(null);
        statusInput.setValue(CustomerStatus.ACTIVE);
        historyArea.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    private void showCustomerHistory(Customer customer) {
        this.historyArea.setText(customerHistoryText(customer));
    }

    String customerHistoryText(Customer customer) {
        return this.rentService.readAllRents().stream()
                .filter(rent -> rent.getCustomer().equals(customer))
                .sorted(Comparator.comparing(Rent::getStartDate).reversed())
                .map(this::formatRentLine)
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .orElse("No rentals yet.");
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
        LocalDate birthdate = birthdayInput.getValue();
        if (birthdate == null) {
            showValidationError("Please pick a birthday.");
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
            customerService.updateCustomer(currentCustomer, firstnameInput.getText(), lastnameInput.getText(), birthdate);
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

    private static void showValidationError(@Nullable String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        Theme.apply(alert.getDialogPane());
        alert.showAndWait();
    }

    static int pageCount(int totalSize) {
        return Math.max(1, (int) Math.ceil((double) totalSize / PAGE_SIZE));
    }

    static int clampPageIndex(int pageIndex, int pageCount) {
        return Math.clamp(pageIndex, 0, pageCount - 1);
    }

    private void updatePage(int pageIndex) {
        int pageCount = pageCount(this.sortedCustomers.size());
        this.currentPageIndex = clampPageIndex(pageIndex, pageCount);
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
