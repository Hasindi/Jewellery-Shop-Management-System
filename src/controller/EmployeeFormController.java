package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Employee;
import model.Stock;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class EmployeeFormController {
    public AnchorPane employeeContext;
    public TextField txtSearchName;
    public TableView<Employee> tblAllEmployee;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colNIC;
    public TableColumn colEmail;
    public TableColumn colContact;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtNIC;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public TextField txtSearchId;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory("NIC"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contactNo"));


        try {
            loadAllEmployees();
            autoId();
        } catch (NullPointerException ee) {
            ee.printStackTrace();
        }


        tblAllEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });


        Pattern namePattern = Pattern.compile("^[A-z]{3,10}[ ][A-z]{3,10}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{5,30}$");
        Pattern nicPattern = Pattern.compile("^([0-9]{9}[v]|[0-9]{12})$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9!#$%&*+/=?_]{3,}(@gmail.com)$");
        Pattern contactPattern = Pattern.compile("^(0)(71|77|76|70|75|78|91)-[0-9]{7}$");

        map.put(txtName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtNIC, nicPattern);
        map.put(txtEmail, emailPattern);
        map.put(txtContact, contactPattern);

    }

    private void setData(Employee e) {
        txtId.setText(e.getId());
        txtName.setText(e.getName());
        txtAddress.setText(e.getAddress());
        txtNIC.setText(e.getNIC());
        txtEmail.setText(e.getEmail());
        txtContact.setText(e.getContactNo());

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    public void autoId() {
        try {
            ResultSet result = CrudUtil.execute("SELECT eId FROM Employee ORDER BY eId DESC LIMIT 1");

            if (result.next()) {

                String numRun = result.getString("eId");
                int col = numRun.length();

                String num1 = numRun.substring(0, 2);//first  (EI)
                String num2 = numRun.substring(2, col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1 + num3;
                txtId.setText(fullnum);

            } else {
                txtId.setText("EI1000");
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
    }

    private void loadAllEmployees() throws SQLException, ClassNotFoundException {

        try{
            ResultSet result = CrudUtil.execute("SELECT * FROM Employee");

            ObservableList<Employee> obList = FXCollections.observableArrayList();

            while (result.next()) {
                obList.add(
                        new Employee(
                                result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6)
                        )
                );
            }
            tblAllEmployee.setItems(obList);


            FilteredList<Employee> filterData = new FilteredList<Employee>(obList, b -> true);

            txtSearchId.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Employee -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Employee.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Employee -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Employee.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<Employee> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblAllEmployee.comparatorProperty());
            tblAllEmployee.setItems(sortedData);

        }catch (ClassNotFoundException| SQLException ee){
            ee.printStackTrace();
        }

    }

    public void addEmployeeOnAction(ActionEvent actionEvent) {
        Employee e = new Employee(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtNIC.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try {

            if (CrudUtil.execute("INSERT INTO Employee VALUES (?,?,?,?,?,?)", e.getId(), e.getName(), e.getAddress(), e.getNIC(), e.getEmail(), e.getContactNo())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...!!!").show();

                loadAllEmployees();
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();

            new Alert(Alert.AlertType.ERROR, ee.getMessage()).show();
        }
        clearText();
        autoId();
    }

    private void clearText() {
        txtName.clear();
        txtAddress.clear();
        txtNIC.clear();
        txtEmail.clear();
        txtContact.clear();
        autoId();
        tblAllEmployee.refresh();
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee e = new Employee(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtNIC.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try {

            if (CrudUtil.execute("UPDATE Employee SET name=?, address=?, NIC=?, email=?, contactNo=? WHERE eId=?", e.getName(), e.getAddress(), e.getNIC(), e.getEmail(), e.getContactNo(), e.getId())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!!!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            }

        } catch (SQLException | ClassNotFoundException ee) {
            ee.printStackTrace();
        }
        loadAllEmployees();
        clearText();
    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {

            if (CrudUtil.execute("DELETE FROM Employee WHERE eId=?", txtId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again...!").showAndWait();
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
        loadAllEmployees();
    }

    public void textFieldsReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response = ValidationUtil.validate(map,btnAdd);

            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearText();
    }

    public void printOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String NIC = txtNIC.getText();
        String email = txtEmail.getText();
        String contactNo = txtContact.getText();

        HashMap paramMap = new HashMap();

        paramMap.put("id",id);
        paramMap.put("name",name);
        paramMap.put("address",address);
        paramMap.put("NIC",NIC);
        paramMap.put("email",email);
        paramMap.put("contactNo",contactNo);

        try{
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/Report/AllEmployeeDetails.jasper"));
            ObservableList<Employee> items = tblAllEmployee.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblAllEmployee.getItems().toArray()));
            JasperViewer.viewReport(jasperPrint,false);

        }catch(JRException ex){
            ex.printStackTrace();
        }
    }
}





