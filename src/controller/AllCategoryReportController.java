package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Stock;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AllCategoryReportController {
    public AnchorPane categoryContext;
    public TextField txtSearch;
    public TextField txtSearchName;
    public TableView <Stock>tblAllOrder;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colDesc;
    public TableColumn colQty;
    public TableColumn colUnitPrice;

    public void initialize() throws ClassNotFoundException, SQLException {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("stockItem"));
        colDesc.setCellValueFactory(new PropertyValueFactory("description"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));

        loadAllCategories();
    }

    private void loadAllCategories() throws ClassNotFoundException, SQLException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Stock");

        ObservableList<Stock> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Stock(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getInt(4),
                            result.getDouble(5)
                    )
            );

        }
        tblAllOrder.setItems(obList);

        FilteredList<Stock> filterData = new FilteredList<Stock>(obList, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Stock.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false;
            });
        });
        txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Stock.getStockItem().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else
                    return false;
            });
        });
        SortedList<Stock> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tblAllOrder.comparatorProperty());
        tblAllOrder.setItems(sortedData);
    }
}
