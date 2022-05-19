package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Supplier;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SupplierFormController {

    public AnchorPane supplierContext;
    public TableView tblAllSupplier;
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
    public TextField txtSearchName;
    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public TextField txtSearchId;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("supId"));
        colName.setCellValueFactory(new PropertyValueFactory("supName"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contactNo"));


        try {
            loadAllSuppliers();
            autoId();
        } catch (NullPointerException ee) {
            ee.printStackTrace();
        }


        tblAllSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData((Supplier) newValue);
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

    private void setData(Supplier s) {
        txtId.setText(s.getSupId());
        txtName.setText(s.getSupName());
        txtAddress.setText(s.getAddress());
        txtNIC.setText(s.getNic());
        txtEmail.setText(s.getEmail());
        txtContact.setText(s.getContactNo());

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    public void autoId() {
        try {
            ResultSet result = CrudUtil.execute("SELECT supId FROM Supplier ORDER BY supId DESC LIMIT 1");

            if (result.next()) {

                String numRun = result.getString("supId");
                int col = numRun.length();

                String num1 = numRun.substring(0, 2);//first  (SI)
                String num2 = numRun.substring(2, col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1 + num3;
                txtId.setText(fullnum);

            } else {
                txtId.setText("SI1000");
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
    }

    private void loadAllSuppliers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier");

        ObservableList<Supplier> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new Supplier(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getString(6)
                    )
            );
            tblAllSupplier.setItems(obList);

            FilteredList<Supplier> filterData = new FilteredList<Supplier>(obList, b -> true);

            txtSearchId.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Supplier -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Supplier.getSupId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Supplier -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Supplier.getSupName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<Supplier> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblAllSupplier.comparatorProperty());
            tblAllSupplier.setItems(sortedData);
        }
    }

    public void addSupplierOnAction(ActionEvent actionEvent) {
        Supplier s = new Supplier(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtNIC.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try {

            if (CrudUtil.execute("INSERT INTO Supplier VALUES (?,?,?,?,?,?)", s.getSupId(), s.getSupName(), s.getAddress(), s.getNic(), s.getEmail(), s.getContactNo())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...!!!").show();

                loadAllSuppliers();
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
        tblAllSupplier.refresh();
    }

    public void updateSupplierOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Supplier s = new Supplier(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtNIC.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try{

            if(CrudUtil.execute("UPDATE Supplier SET supName=?, address=?, nic=?, email=?, contactNo=? WHERE supId=?",s.getSupName(),s.getAddress(),s.getNic(),s.getEmail(),s.getContactNo(),s.getSupId())){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!!!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            }

        }catch(SQLException | ClassNotFoundException ee){
            ee.printStackTrace();
        }
        loadAllSuppliers();
        clearText();
    }

    public void deleteSupplierOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {

            if (CrudUtil.execute("DELETE FROM Supplier WHERE supId=?", txtId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again...!").showAndWait();
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
        loadAllSuppliers();
    }

    public void txtFieldsReleased(KeyEvent keyEvent) {
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

    public void startCellOnAction(ActionEvent actionEvent) throws IOException {
        supplierContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/BuyProductForm.fxml"));
        supplierContext.getChildren().add(parent);
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearText();
    }
}
