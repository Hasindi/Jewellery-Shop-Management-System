package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.AddWindowUi;
import util.CloseWindowUi;
import util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class AdminDashboardFormController implements AddWindowUi, CloseWindowUi {
    public Label lblDate;
    public Label lblTime;
    public Button btnDashboard;
    public Button btnCategory;
    public Button btnAddProduct;
    public Button btnOrder;
    public Button AllOrderOnAction;
    public Button btnSuppliers;
    public Button btnCustomer;
    public Button btnEmployee;
    public Button btnAllOrders;
    public AnchorPane loadFormContext;
    public AnchorPane AdminDashContext;
    public Label lblTotalIncome;
    public Label lblTotalExpensive;
    public Label lblTotalProfit;
    public Label lblCustomerCount;
    public Label lblSupplierOrderCount;
    public Label lblSupplierCount;
    public Label lblCustomerOrderCount;
    public Label lblStaffCount;
    public Label lblItemCount;
    public Label lblDay;

    public void initialize() {
        DateTime();

        try {
            loadAllDashLabels();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        calculateIncome();
        calculateCost();
        calculateProfit();
    }

    private void loadAllDashLabels() throws SQLException, ClassNotFoundException {
        ResultSet result1 = CrudUtil.execute("SELECT COUNT(*)  FROM Customer");
        result1.next();
        lblCustomerCount.setText(String.valueOf(result1.getString(1)));

        ResultSet result2 = CrudUtil.execute("SELECT COUNT(*)  FROM Supplier");
        result2.next();
        lblSupplierCount.setText(String.valueOf(result2.getString(1)));

        ResultSet result3 = CrudUtil.execute("SELECT COUNT(*)  FROM CusOrder");
        result3.next();
        lblCustomerOrderCount.setText(String.valueOf(result3.getString(1)));

        ResultSet result4 = CrudUtil.execute("SELECT COUNT(*)  FROM SupOrder");
        result4.next();
        lblSupplierOrderCount.setText(String.valueOf(result4.getString(1)));

        ResultSet result5 = CrudUtil.execute("SELECT COUNT(*)  FROM Employee");
        result5.next();
        lblStaffCount.setText(String.valueOf(result5.getString(1)));

        ResultSet result6 = CrudUtil.execute("SELECT COUNT(*)  FROM Stock");
        result6.next();
        lblItemCount.setText(String.valueOf(result6.getString(1)));

    }

    private void DateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();

            lblDay.setText(LocalDate.now().getDayOfWeek().toString());
            lblDate.setText(currentDate.getYear()+"-"+ currentDate.getMonthValue()
                    +"-"+ currentDate.getDayOfMonth());

            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":"+ currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void AdminInfoOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AdminInfo.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void DashboardOnAction(ActionEvent actionEvent) throws IOException {
        CloseWindowUi(AdminDashContext);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AdminDashboardForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void CategoryOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AllCategoryReport");
    }

    public void AddProductOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("BuyProductForm");
    }

    public void OrderOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("TakeOrderForm");
    }

    public void GoldRateOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("RateGoldForm");
    }

    public void SuppliersOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("SupplierForm");
    }

    public void CustomerOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("CustomerForm");
    }

    public void EmployeeOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("EmployeeForm");
    }

    public void  calculateIncome(){
        try{
            ResultSet result = CrudUtil.execute("SELECT SUM(total) FROM CusOrderDetail");
            result.next();
            String s = String.valueOf(result.getDouble(1));
            lblTotalIncome.setText(s);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void  calculateCost(){
        try{
            ResultSet result = CrudUtil.execute("SELECT SUM(total) FROM SupOrderDetail");
            result.next();
            String s = String.valueOf(result.getDouble(1));
            lblTotalExpensive.setText(s);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public void  calculateProfit(){
            double q = Double.parseDouble(lblTotalExpensive.getText());
            double q2 = Double.parseDouble(lblTotalIncome.getText());
            double result = q2 - q;

            lblTotalProfit.setText(String.valueOf(result));
    }

    public void LogoutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure...?", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            Stage stage = (Stage) AdminDashContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            stage.centerOnScreen();
        }
    }

    @Override
    public void loadUi(String location) throws IOException {
        loadFormContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        loadFormContext.getChildren().add(parent);
    }

    @Override
    public void CloseWindowUi(AnchorPane a) throws IOException {
        Stage stage= (Stage)a.getScene().getWindow();
        stage.close();
    }

}
