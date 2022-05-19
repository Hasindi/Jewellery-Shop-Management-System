package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import util.CloseWindowUi;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SignUpFormController implements CloseWindowUi {
    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public AnchorPane signUpContext;
    public JFXButton btnSignUp;
    public JFXPasswordField pwdPassword;

    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    LinkedHashMap<JFXPasswordField, Pattern> map1 = new LinkedHashMap<>();

    public void initialize() {
        Pattern userNamePattern = Pattern.compile("^[A-z]{3,10}$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9!#$%&*+/=?_]{3,}(@gmail.com)$");
        Pattern passwordPattern = Pattern.compile("^[A-z0-9]{8}$");

        map.put(txtUserName,userNamePattern);
        map.put(txtEmail,emailPattern);
        map1.put(pwdPassword,passwordPattern);

    }

    public void signUpOnAction(ActionEvent actionEvent) throws IOException {
        if(!(txtUserName.getText().equals("") ||
                txtEmail.getText().equals("") || pwdPassword.getText().equals("") )){

            User u = new User(
                    txtUserName.getText(),txtEmail.getText(),pwdPassword.getText());
            try {
                loadUi();
                if(CrudUtil.execute("INSERT INTO Login VALUES(?,?,?)",u.getUserName(),u.getEmail(),u.getPassword())){
                }
            }catch (ClassNotFoundException | SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Something went wrong...!!!").show();
        }
    }

    private void clearText() {
        txtUserName.clear();
        txtEmail.clear();
        pwdPassword.clear();
    }

    public void btnSignOnAction(ActionEvent actionEvent) throws IOException {
        signUpContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        signUpContext.getChildren().add(parent);
    }

    public void TextFieldsReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnSignUp);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnSignUp);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                if (response instanceof JFXPasswordField) {
                    JFXPasswordField passwordField = (JFXPasswordField) response;
                    passwordField.requestFocus();
                }
            }
        }
    }

    public void loadUi() throws IOException {
        CloseWindowUi(signUpContext);
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    @Override
    public void CloseWindowUi(AnchorPane a) throws IOException {
        Stage stage= (Stage)a.getScene().getWindow();
        stage.close();
    }
}
