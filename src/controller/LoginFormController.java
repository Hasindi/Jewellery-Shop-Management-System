package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public JFXTextField txtUserName;
    public AnchorPane logInContext;
    public JFXButton btnLogIn;
    public Label lbl1;
    public JFXPasswordField pwdPassword;

    public void signInOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String UserId = null;

        if (txtUserName.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "Please Try Again...!").show();
        }else {
            String userName = null;
            boolean user = true;
            boolean next = false;
            boolean password = true;

            ResultSet resultset = CrudUtil.execute("SELECT * FROM Login");

            while (resultset.next()){
                String username = resultset.getString(1);

                if (username.equals(txtUserName.getText())) {
                    UserId = resultset.getString(1);
                    userName = username;
                    user = false;
                    next = true;
                }
            }

            if (user){
                new Alert(Alert.AlertType.ERROR, "Invalid UserName. Please Try Again...!").show();
                password = false;
            }

            if (next){
                if (pwdPassword.getText().equals("")){
                    new Alert(Alert.AlertType.ERROR, "Please Try Again...!").show();
                    password=false;
                }else {
                    ResultSet resultSet = CrudUtil.execute("SELECT * FROM Login WHERE userName=?",txtUserName.getText());

                    while (resultSet.next()){
                        if (resultSet.getString(3).equals(pwdPassword.getText())){
                            Stage stage = (Stage)logInContext.getScene().getWindow();
                            stage.close();

                            URL resource = getClass().getResource("../view/AdminDashboardForm.fxml");
                            Parent load = FXMLLoader.load(resource);
                            Scene scene = new Scene(load);
                            Stage stage1 = new Stage();
                            stage1.setScene(scene);
                            stage1.centerOnScreen();
                            stage1.show();

                            password=false;
                        }
                    }
                }
            }

            if (password){
                new Alert(Alert.AlertType.ERROR, "Invalid Password. Please Try Again...!").show();
            }
        }
        clearText();
    }

    private void clearText() {
        txtUserName.clear();
        pwdPassword.clear();
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) throws IOException {
        logInContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/SignUpForm.fxml"));
        logInContext.getChildren().add(parent);
    }

    public void click(MouseEvent mouseEvent) throws IOException {
        logInContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/SignUpForm.fxml"));
        logInContext.getChildren().add(parent);
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
    }
}
