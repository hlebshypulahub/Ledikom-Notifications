package org.openjfx.utilities.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.openjfx.ledicom.entities.Employee;
import org.openjfx.ledicom.entities.EmployeeContract;
import org.openjfx.ledicom.entities.Task;
import org.openjfx.utilities.converters.SqlDateStringConverter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static javax.swing.JOptionPane.showMessageDialog;

public class DatabaseController {
    protected static Connection conn = connect();

    public static Connection connect() {
        String propFileName = "config.properties";

        String dbURL;
        String dbUser;
        String dbPassword;

        Connection conn = null;

        try (InputStream inputStream = DatabaseController.class.getClassLoader().getResourceAsStream(propFileName)) {
            Properties prop = new Properties();

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

//            /// EXTERNAL
//            dbURL = prop.getProperty("externalURL");
//            dbUser = prop.getProperty("externalUser");
//            dbPassword = prop.getProperty("externalPassword");
//            /// INTERNAL
//            dbURL = prop.getProperty("internalURL");
//            dbUser = prop.getProperty("internalUser");
//            dbPassword = prop.getProperty("internalPassword");
            /// LOCAL
            dbURL = prop.getProperty("localURL");
            dbUser = prop.getProperty("localUser");
            dbPassword = prop.getProperty("localPassword");

            try {
                conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            } catch (SQLException e) {
                showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return conn;
    }

    public static void close() throws SQLException {
        conn.close();
    }

    public static ObservableList<Employee> dobNotificationsEmployeeList() throws SQLException {
        String sql = "select * from get_employee_dob_notifications();";
        return employeeList(sql);
    }

    public static ObservableList<Employee> employeeList(String sql) throws SQLException {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setDOB(SqlDateStringConverter.sqlDateToString(rs.getDate("out_dob")));
                employee.setName(rs.getString("out_name"));
                employee.setDobAge(rs.getInt("out_age"));
                employeeList.add(employee);
            }
            return employeeList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showMessageDialog(null, e.getMessage());
            throw e;
        }
    }

    public static ObservableList<EmployeeContract> employeeContractList() throws SQLException {
        String sql = "select * from get_employee_contract_notifications();";

        ObservableList<EmployeeContract> employeeContractList = FXCollections.observableArrayList();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeContract employeeContract = new EmployeeContract();
                employeeContract.setEmployeeName(rs.getString("out_employee_name"));
                employeeContract.setContractFullInfo(SqlDateStringConverter.sqlDateToString(rs.getDate("out_start_date"))
                        + " - " + SqlDateStringConverter.sqlDateToString(rs.getDate("out_expiration_date"))
                        + " (" + rs.getString("out_contract_info") + ")");
                employeeContractList.add(employeeContract);
            }
            return employeeContractList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showMessageDialog(null, e.getMessage());
            throw e;
        }
    }

    public static ObservableList<Task> employeeTasks() throws SQLException {
        String sql = "select * from task order by date;";

        ObservableList<Task> observableList = FXCollections.observableArrayList();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id_task"));
                task.setEmployee(getEmployee(rs.getInt("id_employee")));
                task.setDate(SqlDateStringConverter.sqlDateToString(rs.getDate("date")));
                task.setTask(rs.getString("description"));
                observableList.add(task);
            }
            return observableList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showMessageDialog(null, e.getMessage());
            throw e;
        }
    }

    public static Employee getEmployee(int employeeId) {
        String sql = "SELECT * FROM employee_data_view WHERE id_employee = " + employeeId + ";";

        Employee employee = new Employee();

        try (
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                employee.setName(rs.getString("last_name") + " "
                        + rs.getString("first_name").charAt(0) + ". "
                        + rs.getString("patronymic").charAt(0) + ".");
            }
            return employee;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showMessageDialog(null, e.getMessage());
            return null;
        }
    }

    public static void deleteTask(int id) throws SQLException {
        String sql = "delete from task where id_task = " + id;

        psExecute(sql);
    }

    public static void psExecute(String sql) throws SQLException {
        try (
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
