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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerFormController {
    public AnchorPane customerContext;
    public TableView<Customer> tblAllCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public TableColumn colContact;
    public JFXTextField txtAddress;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    public TextField txtSearchId;
    public TextField txtSearchName;

    LinkedHashMap<JFXTextField,Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contactNo"));


        try{
            loadAllCustomers();
            autoId();
        }catch(NullPointerException e){
            e.printStackTrace();
        }


        tblAllCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
            }
        });


        Pattern namePattern = Pattern.compile("^[A-z]{3,10}[ ][A-z]{3,10}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{5,30}$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9!#$%&*+/=?_]{3,}(@gmail.com)$");
        Pattern contactPattern = Pattern.compile("^(0)(71|77|76|70|75|78|91)-[0-9]{7}$");

        map.put(txtName,namePattern);
        map.put(txtAddress,addressPattern);
        map.put(txtEmail,emailPattern);
        map.put(txtContact,contactPattern);

    }

    private void setData(Customer c) {
        txtId.setText(c.getId());
        txtName.setText(c.getName());
        txtAddress.setText(c.getAddress());
        txtEmail.setText(c.getEmail());
        txtContact.setText(c.getContactNo());

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }


    public void autoId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");

            if(result.next()){

                String numRun = result.getString("id");
                int col = numRun.length();

                String num1 = numRun.substring(0,2);//first  (CI)
                String num2 = numRun.substring(2,col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1+num3;
                txtId.setText(fullnum);

            }else{
                txtId.setText("CI1000");
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void loadAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Customer(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5)
                    )
            );
            tblAllCustomer.setItems(obList);

            FilteredList<Customer> filterData = new FilteredList<Customer>(obList, b -> true);

            txtSearchId.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Customer.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Customer.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<Customer> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblAllCustomer.comparatorProperty());
            tblAllCustomer.setItems(sortedData);
        }
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       Customer c=new Customer(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try{

            if(CrudUtil.execute("UPDATE Customer SET name=?, address=?, email=?, contactNo=? WHERE id=?",c.getName(),c.getAddress(),c.getEmail(),c.getContactNo(),c.getId())){
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!!!").show();
            }else{
                 new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            }

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        loadAllCustomers();
        clearText();
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try{

            if(CrudUtil.execute("DELETE FROM Customer WHERE id =?",txtId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted...!!!").showAndWait();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION,"Try Again...!").showAndWait();
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        loadAllCustomers();
    }

    public void addCustomerOnAction(ActionEvent actionEvent) {
        Customer c=new Customer(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtContact.getText()
        );

        try{

            if(CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?,?)",c.getId(),c.getName(),c.getAddress(),c.getEmail(),c.getContactNo())){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved...!!!").show();

                loadAllCustomers();
            }

        }catch(ClassNotFoundException  | SQLException e){
            e.printStackTrace();

            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        clearText();
        autoId();
    }

    private void clearText() {
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtContact.clear();
        autoId();
        tblAllCustomer.refresh();
    }

    public void startCellOnAction(ActionEvent actionEvent) throws IOException {
        customerContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/TakeOrderForm.fxml"));
        customerContext.getChildren().add(parent);
    }


    public void TextFieldsReleased(KeyEvent keyEvent) {
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
}
