package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class AdminInfoController {
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtEmail;
    public TextField txtContact;
    public TextField txtNIC;

    public void EditOnAction(ActionEvent actionEvent) {
        clearText();
    }

    private void clearText() {
        txtName.clear();
        txtAddress.clear();
        txtNIC.clear();
        txtContact.clear();
        txtEmail.clear();
    }

    public void SaveOnAction(ActionEvent actionEvent) {
    }
}
