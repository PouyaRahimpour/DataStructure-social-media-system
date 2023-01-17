package com.example.instagram;

import com.example.instagram.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInPageController implements Initializable {
    private static Parent root;
    private static Stage stage;
    private static Scene scene;
    @FXML
    private Label message;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void switchToSignUpPage() throws IOException {
         PageSwitcher.switchToPage("signUpPage.fxml");
    }

    public void logIn() throws IOException {
        User user = SqlManager.getInstance().findUser(username.getText());
        if (user.getUsername()!=null) {
            if (user.getPassword().equals(password.getText())) {
                PersonalPageController.setUsername(user.getUsername());
                PageSwitcher.switchToPage("personalPage.fxml");
            } else {
                message.setText("Password doesn't match");
            }
        } else {
            message.setText("Couldn't find user with this username");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setText("");
    }

}