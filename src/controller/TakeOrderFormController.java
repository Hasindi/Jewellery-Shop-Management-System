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
import model.CusOrder;
import model.CusOrderDetails;
import model.Customer;
import model.Stock;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
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

public class TakeOrderFormController {
    public AnchorPane orderFormcontext;
    public JFXTextField txtOrderId;
    public JFXTextField txtOrderDate;
    public ComboBox<String> cmbCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCustomerEmail;
    public TextField txtCustomerContact;
    public TextField txtLastCustomerId;
    public ComboBox<String> cmbItemCode;
    public TextField txtDesc;
    public TextField txtQtyOnHand;
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
    public TextField txtSelectproduct;
    public Button btnAddToCart;

    public void initialize() {
        btnAddToCart.setDisable(true);

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("stockItem"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setCustomerIds();
        setItemCode();
        LastCId();
        autoOrderId();
        loadDate();

        cmbCustomerId.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setCustomerDetails(newValue);
                });

        cmbItemCode.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setItemDetails(newValue);
                });

    }

    private void loadDate() {
        txtOrderDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }


    public void autoOrderId() {
        try {
            ResultSet result = CrudUtil.execute("SELECT id FROM CusOrder ORDER BY id DESC LIMIT 1");

            if (result.next()) {
                String numRun = result.getString("id");
                int col = numRun.length();

                String num1 = numRun.substring(0, 3);//first  (COI)
                String num2 = numRun.substring(3, col);//last (1000)

                int n = Integer.parseInt(num2);
                n++;

                String num3 = Integer.toString(n);
                String fullnum = num1 + num3;
                txtOrderId.setText(fullnum);

            } else {
                txtOrderId.setText("COI1000");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void LastCId() {
        try {
            ResultSet result = CrudUtil.execute("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");

            if (result.next()) {
                String numRun = result.getString("id");
                txtLastCustomerId.setText(numRun);
            } else {
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setItemDetails(String selectedItemCode) {
        try {
            Stock i = ItemCrudController.getItem(selectedItemCode);

            if (i != null) {
                txtSelectproduct.setText(String.valueOf(i.getStockItem()));
                txtDesc.setText(String.valueOf(i.getDescription()));
                txtQtyOnHand.setText(String.valueOf(i.getQty()));
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        btnAddToCart.setDisable(false);
    }

    private void setCustomerDetails(String selectedCustomerId) {
        try {
            Customer c = CustomerCrudController.getCustomer(selectedCustomerId);

            if (c != null) {
                txtCustomerName.setText(c.getName());
                txtCustomerAddress.setText(c.getAddress());
                txtCustomerEmail.setText(c.getEmail());
                txtCustomerContact.setText(c.getContactNo());
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
    }

    private void setItemCode() {
        try {
            cmbItemCode.setItems(FXCollections.observableArrayList(ItemCrudController.getItemCodes()));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCustomerIds() {

        try {

            ObservableList<String> cIdObList = FXCollections.observableArrayList(
                    CustomerCrudController.getCustomerId()
            );
            cmbCustomerId.setItems(cIdObList);


        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void AddToCartOnAction(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double totalCost = unitPrice * qty;
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        String description = txtDesc.getText();
        String stockItem = txtSelectproduct.getText();

        if (qtyOnHand < qty) {
            new Alert(Alert.AlertType.WARNING, "Out Of Stock...!").show();
            return;
        }

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
        txtQty.clear();
    }

    private void quntityChange() {
        int value = Integer.parseInt(txtQtyOnHand.getText());
        if (!txtQty.getText().equals("") & (value > 0)) {
            int q = Integer.parseInt(txtQty.getText());
            int q2 = Integer.parseInt(txtQtyOnHand.getText());
            int result = q2 - q;
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : tmList) {
            total += tm.getTotalCost();
        }
        txtTotal.setText(String.valueOf(total));
    }

    private CartTM isExists(String itemCode) {
        for (CartTM tm : tmList) {
            if (tm.getId().equals(itemCode)) {
                return tm;
            }
        }
        return null;
    }

    public void PlaceOrderOnAction(ActionEvent actionEvent) throws SQLException {
        autoOrderId();
        String s = txtOrderId.getText();

        CusOrder order = new CusOrder(
                s,
                txtOrderDate.getText(),
                cmbCustomerId.getValue()
        );

        ArrayList<CusOrderDetails> details = new ArrayList<>();

        for (CartTM tm : tmList
        ) {
            details.add(
                    new CusOrderDetails(
                            s,
                            tm.getId(),
                            tm.getQty(),
                            tm.getUnitPrice(),
                            tm.getTotalCost()
                    )
            );
        }

        Connection connection = null;

        try {

            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = new CusOrderCrudController().saveOrder(order);
            if (isOrderSaved) {
                boolean isDetailsSaved = new CusOrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved) {
                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Successfully...!").showAndWait();
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error...!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Error...!").show();
            }

        } catch (SQLException | ClassNotFoundException e) {

        } finally {
            connection.setAutoCommit(true);
        }
        clearText1();
    }

    private void clearText1() {
        cmbCustomerId.setValue("");
        txtCustomerEmail.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerContact.clear();
        cmbItemCode.setValue("");
        txtSelectproduct.clear();
        txtDesc.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtQty.clear();
        tblItemDetails.getItems().clear();
    }

    public void backCustomerFormOnAction(ActionEvent actionEvent) throws IOException {
        orderFormcontext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"));
        orderFormcontext.getChildren().add(parent);
    }

    public void printOnAction(ActionEvent actionEvent) {
        String orderId = txtOrderId.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();
        String contactNo = txtCustomerContact.getText();
        double total = Double.parseDouble(txtTotal.getText());

        HashMap paramMap = new HashMap();

        paramMap.put("orderId", orderId);
        paramMap.put("name", name);
        paramMap.put("address", address);
        paramMap.put("contactNo", contactNo);
        paramMap.put("total", total);


        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/Report/TakeOrder1.jasper"));
            ObservableList<Stock> items = tblItemDetails.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblItemDetails.getItems().toArray()));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            ex.printStackTrace();
        }

    }
}
