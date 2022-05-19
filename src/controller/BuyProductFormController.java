package controller;

import Database.DBConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import util.CrudUtil;
import view.TM.CartTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BuyProductFormController {
    public AnchorPane buyProductContext;
    public JFXTextField txtOrderId;
    public JFXTextField txtOrderDate;
    public ComboBox<String> cmbSupplierId;
    public TextField txtSupplierName;
    public TextField txtSupplierAddress;
    public TextField txtSupplierNIC;
    public TextField txtSupplierEmail;
    public TextField txtSupplierContact;
    public TextField txtLastSupplierId;
    public ComboBox<String> cmbItemCode;
    public TextField txtDesc;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public TableView tblItemDetails;
    public TableColumn colItemCode;
    public TableColumn colProductName;
    public TableColumn colDesc;
    public TableColumn colqty;
    public TableColumn colUnitPrice;
    public TableColumn colTotalCost;
    public TableColumn colAction;
    public TextField txtTotal;
    public TextField txtQtyOnHand;
    public TextField txtSelectproduct;
    public Button btnAddToCart;

    public void initialize(){
        btnAddToCart.setDisable(true);

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("stockItem"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));


        setSupplierIds();
        setItemCode();
        LastSId();
        autoOrderId();
        loadDate();

        cmbSupplierId.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            setSupplierDetails(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            setItemDetails(newValue);
        });

    }

    private void loadDate(){
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }


    public void autoOrderId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT id FROM SupOrder ORDER BY id DESC LIMIT 1");

            if(result.next()){

                String numRun = result.getString("id");
                int col = numRun.length();

                String num1 = numRun.substring(0,3);//first  (SOI)
                String num2 = numRun.substring(3,col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1+num3;
                txtOrderId.setText(fullnum);

            }else{
                txtOrderId.setText("SOI1000");
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void LastSId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT supId FROM Supplier ORDER BY supId DESC LIMIT 1");

            if(result.next()){
                String numRun = result.getString("supId");
                txtLastSupplierId.setText(numRun);
            }else{
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private void setItemDetails(String selectedItemCode) {
        try{
            Stock i = ItemCrudController.getItem(selectedItemCode);

            if(i!= null){
                txtSelectproduct.setText(String.valueOf(i.getStockItem()));
                txtDesc.setText(String.valueOf(i.getDescription()));
                txtQtyOnHand.setText(String.valueOf(i.getQty()));
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));

            }

        }  catch (SQLException e){
            e.printStackTrace();
        }  catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        btnAddToCart.setDisable(false);

    }

    private void setSupplierDetails(String selectedSupplierId) {
        try{
            Supplier s = SupplierCrudController.getSupplier(selectedSupplierId);

            if(s!=null){
                txtSupplierName.setText(s.getSupName());
                txtSupplierAddress.setText(s.getAddress());
                txtSupplierNIC.setText(s.getNic());
                txtSupplierEmail.setText(s.getEmail());
                txtSupplierContact.setText(s.getContactNo());
            }

        }catch(SQLException e){
            e.printStackTrace();

        }catch(ClassNotFoundException e){
            e.printStackTrace();

        }
    }

    private void setItemCode() {
        try{
            cmbItemCode.setItems(FXCollections.observableArrayList(ItemCrudController.getItemCodes()));

        }   catch (SQLException e){
            e.printStackTrace();
        }   catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void setSupplierIds(){

        try {

            ObservableList<String> sIdObList = FXCollections.observableArrayList(
                    SupplierCrudController.getSupplierId()
            );
            cmbSupplierId.setItems(sIdObList);

        }catch(SQLException e){
            e.printStackTrace();

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void AddToCartOnAction(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double totalCost = unitPrice * qty;

        String description = txtDesc.getText();
        String stockItem = txtSelectproduct.getText();

        CartTM isExists = isExists(cmbItemCode.getValue());
        if (isExists != null) {
            for (CartTM temp : tmList) {
                if (temp.equals(isExists)) {
                    temp.setQty((temp.getQty() + qty));
                    temp.setTotalCost(temp.getTotalCost() + totalCost);
                }
            }
        } else {
            Button btn = new Button("Delete");

            CartTM tm = new CartTM(
                    cmbItemCode.getValue(),
                    stockItem,
                    description,
                    qty,
                    unitPrice,
                    totalCost,
                    btn
            );

            btn.setOnAction(e -> {
                for (CartTM tempTm : tmList) {
                    if (tempTm.getId().equals(tm.getId())) {
                        tmList.remove(tempTm);
                        calculateTotal();
                    }
                }
            });
            tmList.add(tm);
            tblItemDetails.setItems(tmList);
        }
        tblItemDetails.refresh();
        calculateTotal();
        quntityChange();
    }

    private void quntityChange() {
        int value = Integer.parseInt(txtQtyOnHand.getText());
            if(!txtQty.getText().equals("") & (value>0) ){
                int q = Integer.parseInt(txtQty.getText());
                int q2 = Integer.parseInt(txtQtyOnHand.getText());
                int result= q2 + q;

                txtQtyOnHand.setText(String.valueOf(result));
            }
    }

    private void calculateTotal() {
        double total = 0;
        for(CartTM tm: tmList){
            total+=tm.getTotalCost();
        }
        txtTotal.setText(String.valueOf(total));
    }

    private CartTM isExists(String itemCode) {
        for(CartTM tm:tmList){
            if(tm.getId().equals(itemCode)){
                return tm;
            }
        }
        return  null;
    }


    public void PlaceOrderOnAction(ActionEvent actionEvent) throws SQLException {
        autoOrderId();
        String s = txtOrderId.getText();

        SupOrder order = new SupOrder(
                s,
                txtOrderDate.getText(),
                cmbSupplierId.getValue()
        );

        ArrayList<SupOrderDetails> details = new ArrayList<>();

        for (CartTM tm : tmList
        ) {
            details.add(
                    new SupOrderDetails(
                            s,
                            tm.getId(),
                            tm.getQty(),
                            tm.getUnitPrice(),
                            tm.getTotalCost()
                    )
            );
        }

        Connection connection  = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = new SupOrderCrudController().saveOrder(order);
            if (isOrderSaved) {
                boolean isDetailsSaved=new SupOrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved){
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION,"Saved!").showAndWait();
                }else{
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR,"Error!").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Error!").show();
            }

        }catch (SQLException | ClassNotFoundException e){

        }finally {
            connection.setAutoCommit(true);
        }
        clearText();
    }

    private void clearText() {
        cmbSupplierId.setValue("");
        txtSupplierEmail.clear();
        txtSupplierName.clear();
        txtSupplierContact.clear();
        txtSupplierAddress.clear();
        txtSupplierNIC.clear();
        cmbItemCode.setValue("");
        txtSelectproduct.clear();
        txtDesc.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        tblItemDetails.refresh();
    }

    public void backSupplierFormOnAction(ActionEvent actionEvent) throws IOException {
        buyProductContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/SupplierForm.fxml"));
        buyProductContext.getChildren().add(parent);
    }

    public void printOnAction(ActionEvent actionEvent) {
        String orderId = txtOrderId.getText();
        String name = txtSupplierName.getText();
        String address = txtSupplierAddress.getText();
        String contactNo = txtSupplierContact.getText();
        double total = Double.parseDouble(txtTotal.getText());

        HashMap paramMap = new HashMap();

        paramMap.put("orderId",orderId);
        paramMap.put("name",name);
        paramMap.put("address",address);
        paramMap.put("contactNo",contactNo);
        paramMap.put("total",total);


        try{
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/Report/BuyProduct1.jasper"));
            ObservableList<Stock> items = tblItemDetails.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblItemDetails.getItems().toArray()));
            JasperViewer.viewReport(jasperPrint,false);

        }catch(JRException ex){
            ex.printStackTrace();
        }
    }
}
