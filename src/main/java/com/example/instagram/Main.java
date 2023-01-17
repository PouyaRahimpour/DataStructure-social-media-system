package com.example.instagram;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
//        Parent root = new FXMLLoader(Main.class.getResource("logInPage.fxml")).load();
//        Scene scene = new Scene(root);
//        new LogInPageController("");
        new PageSwitcher().switchToPage("logInPage.fxml");
        stage.getIcons().add(new Image(new FileInputStream("src/main/resources/com/example/instagram/images/instagram_logo.png")));
        stage.setTitle("Instagram");
        stage.setResizable(false);
//        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}