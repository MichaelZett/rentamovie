package hh.fernuni.rentamovie.rent.adapter;

import hh.fernuni.rentamovie.customer.application.CustomerService;
import hh.fernuni.rentamovie.customer.domain.Customer;
import hh.fernuni.rentamovie.movie.domain.Copy;
import hh.fernuni.rentamovie.movie.domain.CopyRepository;
import hh.fernuni.rentamovie.rent.application.RentService;
import hh.fernuni.rentamovie.rent.domain.Rent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.ZoneId;

public class RentDialog extends Dialog<Rent> {
	private Label customerLabel = new Label("Customer: ");
	private Label movieLabel = new Label("Movie: ");
	private GridPane grid = new GridPane();
	private ComboBox<Customer> customerBox;
	private ComboBox<Copy> movieBox;

    private CustomerService customerService = CustomerService.getService();
	private CopyRepository copyRepository = CopyRepository.getRepository();
	private RentService rentService = RentService.getService();

	RentDialog() {
		this.setTitle("Input rent");
        ObservableList<Customer> customerOptions = FXCollections.observableArrayList(this.customerService.readActiveCustomers());
        this.customerBox = new ComboBox<>(customerOptions);
        ObservableList<Copy> movieOptions = FXCollections.observableArrayList(this.copyRepository.readAll().stream()
                .filter(copy -> copy.isAvailable() && copy.getMovie().isActive())
                .toList());
        this.movieBox = new ComboBox<>(movieOptions);

		this.grid.add(this.customerLabel, 1, 1);
		this.grid.add(this.customerBox, 2, 1);
		this.grid.add(this.movieLabel, 1, 2);
		this.grid.add(this.movieBox, 2, 2);
		this.getDialogPane().setContent(this.grid);

		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().add(buttonTypeOk);
        this.setResultConverter(b -> {
            if (b != buttonTypeOk || this.movieBox.getValue() == null || this.customerBox.getValue() == null) {
                return null;
            }
            return this.rentService.createRent(this.movieBox.getValue(), this.customerBox.getValue(), LocalDate.now(ZoneId.systemDefault()));
        });
	}

}
