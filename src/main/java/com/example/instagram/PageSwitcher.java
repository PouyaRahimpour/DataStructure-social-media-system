package com.example.instagram;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageSwitcher {
    private static Stage stage = Main.stage;

    public static void switchToPage(String fxml) throws IOException {
        Parent root = FXMLLoader.load(PageSwitcher.class.getResource(fxml));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }


}
