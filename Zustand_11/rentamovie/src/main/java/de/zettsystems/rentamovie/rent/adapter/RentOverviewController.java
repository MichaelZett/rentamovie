package de.zettsystems.rentamovie.rent.adapter;

import de.zettsystems.rentamovie.common.adapter.Theme;
import de.zettsystems.rentamovie.rate.application.RateService;
import de.zettsystems.rentamovie.rent.application.RentService;
import de.zettsystems.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import org.jspecify.annotations.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

// @FXML members are wired via reflection from the FXML file; ErrorProne cannot see those uses.
@SuppressWarnings({"UnusedMethod", "UnusedVariable"})
public class RentOverviewController {
    private static final int PAGE_SIZE = 5;

    private RentService rentService;
    private RateService rateService;
    private final ObservableList<Rent> rents = FXCollections.observableArrayList();
    private final ObservableList<Rent> pageRents = FXCollections.observableArrayList();
    private final FilteredList<Rent> filteredRents = new FilteredList<>(rents, rent -> true);
    private final SortedList<Rent> sortedRents = new SortedList<>(filteredRents);
    private int currentPageIndex;

    public RentOverviewController() {
        this(RentService.getService(), RateService.getService());
    }

    RentOverviewController(RentService rentService, RateService rateService) {
        this.rentService = rentService;
        this.rateService = rateService;
    }

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
    private ComboBox<String> statusFilterBox;
    @FXML
    private Label pageInfoLabel;
    @FXML
    private Button newButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button payButton;
    @FXML
    private Button previousPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Button dailyClosingButton;

    @FXML
    private void initialize() {
        this.customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastname"));
        this.copyColumn.setCellValueFactory(new PropertyValueFactory<>("copyTitle"));
        this.startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.plannedReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("plannedReturnDate"));
        this.endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        this.statusFilterBox.setItems(FXCollections.observableArrayList("All", "Open rents", "Overdue", "Open payments", "Paid"));
        this.statusFilterBox.getSelectionModel().select("All");
        this.sortedRents.comparatorProperty().bind(this.rentTable.comparatorProperty());
        this.rentTable.setItems(this.pageRents);
        this.rentTable.comparatorProperty().addListener((observable, oldValue, newValue) -> updatePage(0));
        this.statusFilterBox.valueProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        refreshRents();
    }

    private void refreshRents() {
        this.rents.setAll(this.rentService.readAllRents());
        updatePage(0);
    }

    private void applyFilter() {
        String selected = this.statusFilterBox.getSelectionModel().getSelectedItem();
        this.filteredRents.setPredicate(rent -> switch (selected) {
            case "Open rents" -> rent.isOpen();
            case "Overdue" -> rent.isOverdue(LocalDate.now(ZoneId.systemDefault()));
            case "Open payments" -> rent.hasOpenPayment();
            case "Paid" -> rent.isPaid();
            default -> true;
        });
        updatePage(0);
    }

    private void updatePage(int pageIndex) {
        int pageCount = Math.max(1, (int) Math.ceil((double) this.sortedRents.size() / PAGE_SIZE));
        this.currentPageIndex = Math.max(0, Math.min(pageIndex, pageCount - 1));
        int fromIndex = this.currentPageIndex * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, this.sortedRents.size());
        this.pageRents.setAll(this.sortedRents.subList(fromIndex, toIndex));
        this.pageInfoLabel.setText((this.sortedRents.isEmpty() ? 0 : this.currentPageIndex + 1) + " / " + pageCount);
        this.previousPageButton.setDisable(this.currentPageIndex == 0);
        this.nextPageButton.setDisable(this.currentPageIndex >= pageCount - 1 || this.sortedRents.isEmpty());
    }

    private void selectWorklist(String worklist) {
        this.statusFilterBox.getSelectionModel().select(worklist);
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
            BigDecimal expectedAmount = expectedPaymentAmount(rent);
            TextInputDialog dialog = new TextInputDialog(expectedAmount.toPlainString());
            dialog.setTitle("Pay rent");
            dialog.setHeaderText("Expected amount: " + expectedAmount.toPlainString());
            dialog.setContentText("Amount:");
            Theme.apply(dialog.getDialogPane());
            dialog.showAndWait().ifPresent(input -> {
                try {
                    this.rentService.payRent(rent, new BigDecimal(input));
                } catch (NumberFormatException _) {
                    showValidationError("Invalid amount '" + input + "'. Please use the format " + expectedAmount.toPlainString() + ".");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    showValidationError(e.getMessage());
                }
            });
            refreshRents();
        }
    }

    @FXML
    private void handleDailyClosing() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Daily closing");
        alert.setHeaderText("Payments on " + today);
        alert.setContentText(dailyClosingText(today));
        Theme.apply(alert.getDialogPane());
        alert.showAndWait();
    }

    BigDecimal expectedPaymentAmount(Rent rent) {
        LocalDate returnDate = rent.getEndDate();
        if (returnDate == null) {
            throw new IllegalStateException("Rent must be returned before payment.");
        }
        int age = Period.between(rent.getCustomer().getBirthdate(), returnDate).getYears();
        return this.rateService.calculatePrice(rent, this.rateService.retrieveRateByAge(age));
    }

    String dailyClosingText(LocalDate date) {
        List<Rent> payments = this.rentService.findPaymentsOn(date);
        StringBuilder text = new StringBuilder();
        if (payments.isEmpty()) {
            text.append("No payments.");
        } else {
            payments.forEach(rent -> text.append(rent.getCustomerLastname())
                    .append(" - ")
                    .append(rent.getCopyTitle())
                    .append(" - ")
                    .append(formatAmount(rent.getPaidAmount()))
                    .append(System.lineSeparator()));
        }
        text.append(System.lineSeparator())
                .append("Total: ")
                .append(formatAmount(this.rentService.sumPaymentsOn(date)));
        return text.toString();
    }

    private static String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static void showValidationError(@Nullable String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(message);
        Theme.apply(alert.getDialogPane());
        alert.showAndWait();
    }

    @FXML
    private void handlePreviousPage() {
        updatePage(this.currentPageIndex - 1);
    }

    @FXML
    private void handleNextPage() {
        updatePage(this.currentPageIndex + 1);
    }

    @FXML
    private void handleAllWorklist() {
        selectWorklist("All");
    }

    @FXML
    private void handleOpenRentsWorklist() {
        selectWorklist("Open rents");
    }

    @FXML
    private void handleOverdueWorklist() {
        selectWorklist("Overdue");
    }

    @FXML
    private void handleOpenPaymentsWorklist() {
        selectWorklist("Open payments");
    }

    @FXML
    private void handlePaidWorklist() {
        selectWorklist("Paid");
    }

}
