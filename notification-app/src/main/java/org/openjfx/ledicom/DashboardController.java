package org.openjfx.ledicom;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.openjfx.ledicom.entities.Employee;
import org.openjfx.ledicom.entities.EmployeeContract;
import org.openjfx.utilities.database.DatabaseController;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Employee> dobTable;

    @FXML
    private TableColumn<Employee, String> nameColD;

    @FXML
    private TableColumn<Employee, String> dobCol;

    @FXML
    private TableColumn<Employee, String> ageCol;

    @FXML
    private TableView<EmployeeContract> contractTable;

    @FXML
    private TableColumn<EmployeeContract, String> nameColC;

    @FXML
    private TableColumn<EmployeeContract, String> contractCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColD.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("DOB"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("dobAge"));

        nameColC.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        contractCol.setCellValueFactory(new PropertyValueFactory<>("contractFullInfo"));

        if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY) {
            dobTable.setPlaceholder(new Label("Сегодня и на выходных дней рождения у сотрудников нет!"));
        } else {
            dobTable.setPlaceholder(new Label("Сегодня дней рождения у сотрудников нет!"));
        }
        contractTable.setPlaceholder(new Label("В течение ближайших 60 дней ни у одного сотрудника контракт не заканчивается!"));

        ObservableList<Employee> employeeList;
        try {
            employeeList = DatabaseController.dobNotificationsEmployeeList();
            dobTable.setItems(employeeList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<EmployeeContract> employeeContractList;
        try {
            employeeContractList = DatabaseController.employeeContractList();
            contractTable.setItems(employeeContractList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
