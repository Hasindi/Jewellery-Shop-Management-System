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
import model.RateGold;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class RateGoldFormController {
    public AnchorPane rateGoldContext;
    public TextField txtSearch;
    public TableView<RateGold>tblAllRateGold;
    public TableColumn col24kt;
    public TableColumn col22kt;
    public TableColumn col21kt;
    public TableColumn col18kt;
    public TableColumn col9kt;
    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXTextField txt24kt;
    public JFXTextField txt22kt;
    public JFXTextField txt21kt;
    public JFXTextField txt18kt;
    public JFXTextField txt9kt;
    public JFXTextField txtId;
    public TableColumn colDate;
    public JFXTextField txtDate;
    public TableColumn colrId;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws ClassNotFoundException, SQLException {

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colrId.setCellValueFactory(new PropertyValueFactory("id") );
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        col24kt.setCellValueFactory(new PropertyValueFactory("kt24"));
        col22kt.setCellValueFactory(new PropertyValueFactory("kt22"));
        col21kt.setCellValueFactory(new PropertyValueFactory("kt21"));
        col18kt.setCellValueFactory(new PropertyValueFactory("kt18"));
        col9kt.setCellValueFactory(new PropertyValueFactory("kt9"));


        try {
            loadAllRates();
            autoId();
        } catch (NullPointerException ee) {
            ee.printStackTrace();
        }


        tblAllRateGold.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });

        Pattern datePattern = Pattern.compile("^(20)[0-9]{2}-(01|02|03|04|05|06|07|08|09|10|11|12)-[0-3][0-9]$");
        Pattern ktPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtDate,datePattern);
        map.put(txt24kt,ktPattern);
        map.put(txt22kt,ktPattern);
        map.put(txt21kt,ktPattern);
        map.put(txt18kt,ktPattern);
        map.put(txt9kt,ktPattern);

    }

    private void setData(RateGold r) {
        txtId.setText(r.getId());
        txtDate.setText(r.getDate());
        txt24kt.setText(String.valueOf(r.getKt24()));
        txt22kt.setText(String.valueOf(r.getKt22()));
        txt21kt.setText(String.valueOf(r.getKt21()));
        txt18kt.setText(String.valueOf(r.getKt18()));
        txt9kt.setText(String.valueOf(r.getKt9()));

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    public void autoId() {
        try {
            ResultSet result = CrudUtil.execute("SELECT rId FROM RateGold ORDER BY rId DESC LIMIT 1");

            if (result.next()) {

                String numRun = result.getString("rId");
                int col = numRun.length();

                String num1 = numRun.substring(0, 2);//first  (RI)
                String num2 = numRun.substring(2, col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1 + num3;
                txtId.setText(fullnum);

            } else {
                txtId.setText("RI1000");
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
    }

    private void loadAllRates() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM RateGold");

        ObservableList<RateGold> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new RateGold(
                            result.getString(1),
                            result.getString(2),
                            result.getDouble(3),
                            result.getDouble(4),
                            result.getDouble(5),
                            result.getDouble(6),
                            result.getDouble(7)
                    )
            );

        }
        tblAllRateGold.setItems(obList);


        FilteredList<RateGold> filterData = new FilteredList<RateGold>(obList, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(RateGold -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (RateGold.getDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<RateGold> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tblAllRateGold.comparatorProperty());
        tblAllRateGold.setItems(sortedData);
    }

    public void addRateOnAction(ActionEvent actionEvent) {
        RateGold r = new RateGold(
                txtId.getText(),
                txtDate.getText(),
                Double.parseDouble(txt24kt.getText()),
                Double.parseDouble(txt22kt.getText()),
                Double.parseDouble(txt21kt.getText()),
                Double.parseDouble(txt18kt.getText()),
                Double.parseDouble(txt9kt.getText())
        );

        try {

            if (CrudUtil.execute("INSERT INTO RateGold VALUES (?,?,?,?,?,?,?)", r.getId(), r.getDate(), r.getKt24(), r.getKt22(), r.getKt21(), r.getKt18(), r.getKt9())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...!!!").show();

                loadAllRates();
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();

            new Alert(Alert.AlertType.ERROR, ee.getMessage()).show();
        }
        clearText();
        autoId();
    }

    private void clearText() {
        txtDate.clear();
        txt24kt.clear();
        txt22kt.clear();
        txt21kt.clear();
        txt18kt.clear();
        txt9kt.clear();
        autoId();
        tblAllRateGold.refresh();
    }

    public void updateRateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        RateGold r = new RateGold(
                txtId.getText(),
                txtDate.getText(),
                Double.parseDouble(txt24kt.getText()),
                Double.parseDouble(txt22kt.getText()),
                Double.parseDouble(txt21kt.getText()),
                Double.parseDouble(txt18kt.getText()),
                Double.parseDouble(txt9kt.getText())
        );

        try {

            if (CrudUtil.execute("UPDATE RateGold SET date=?, kt24=?, kt22=?, kt21=?, kt18=?, kt9=? WHERE rId=?", r.getDate(), r.getKt24(), r.getKt22(), r.getKt21(), r.getKt18(), r.getKt9(), r.getId())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!!!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again...!").show();
            }

        } catch (SQLException | ClassNotFoundException ee) {
            ee.printStackTrace();
        }
        loadAllRates();
        clearText();
    }

    public void deleteRateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {

            if (CrudUtil.execute("DELETE FROM RateGold WHERE rId=?", txtId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again...!").showAndWait();
            }

        } catch (ClassNotFoundException | SQLException ee) {
            ee.printStackTrace();
        }
        loadAllRates();
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
}
