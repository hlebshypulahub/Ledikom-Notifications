package org.openjfx.ledicom;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.openjfx.ledicom.entities.Employee;
import org.openjfx.ledicom.entities.EmployeeContract;
import org.openjfx.ledicom.entities.Task;
import org.openjfx.utilities.database.DatabaseController;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> nameColT;
    @FXML
    private TableColumn<Task, String> dateColT;
    @FXML
    private TableColumn<Task, String> taskColT;

    private void setTaskTable() throws SQLException {
        taskTable.setItems(DatabaseController.employeeTasks());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColD.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("DOB"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("dobAge"));

        nameColC.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        contractCol.setCellValueFactory(new PropertyValueFactory<>("contractFullInfo"));

        nameColT.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        dateColT.setCellValueFactory(new PropertyValueFactory<>("date"));
        taskColT.setCellValueFactory(new PropertyValueFactory<>("task"));

        taskColT.setCellFactory(tc -> {
            TableCell<Task, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(taskColT.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        taskTable.setRowFactory(tableView -> {
            final TableRow<Task> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Удалить");
            removeItem.setOnAction(event -> {
                try {
                    DatabaseController.deleteTask(taskTable.getSelectionModel().getSelectedItem().getId());
                    setTaskTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            rowMenu.getItems().addAll(removeItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));
            return row;
        });

        if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY) {
            dobTable.setPlaceholder(new Label("Сегодня и на выходных дней рождения у сотрудников нет!"));
        } else {
            dobTable.setPlaceholder(new Label("Сегодня дней рождения у сотрудников нет!"));
        }
        contractTable.setPlaceholder(new Label("В течение ближайших 60 дней ни у одного сотрудника контракт не заканчивается!"));
        taskTable.setPlaceholder(new Label("Новых поручений нет!"));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ObservableList<Employee> employeeList;

                ObservableList<EmployeeContract> employeeContractList;

                try {
                    employeeList = DatabaseController.dobNotificationsEmployeeList();
                    dobTable.setItems(employeeList);
                    employeeContractList = DatabaseController.employeeContractList();
                    contractTable.setItems(employeeContractList);
                    setTaskTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0,600000);
    }
}
