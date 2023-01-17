package com.example.instagram;

import com.example.instagram.models.Graph;
import com.example.instagram.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpPageController implements Initializable{
    @FXML
    private Label message;
    @FXML
    private TextField username, fullName, mobileNumber;
    @FXML
    private PasswordField password;

    public void signUp() throws IOException {
        User u = SqlManager.getInstance().findUser(username.getText());
        if (u.getUsername() != null) {
            message.setText("Username already exists.");
        } else {
            message.setText("");
            User user = new User(
                    username.getText(),
                    password.getText(),
                    fullName.getText(),
                    mobileNumber.getText()
            );
            SqlManager.getInstance().addUser(user);
            Graph.getInstance().addUser(user.getUsername());
            PersonalPageController.setUsername(user.getUsername());
            PageSwitcher.switchToPage("personalPage.fxml");
        }
    }
    public void switchToLogInPage() throws IOException {
        PageSwitcher.switchToPage("logInPage.fxml");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setText("");
    }
}
