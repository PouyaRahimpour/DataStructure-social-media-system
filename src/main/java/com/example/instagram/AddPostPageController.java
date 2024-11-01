package com.example.instagram;

import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPostPageController implements Initializable {
    private Stage stage;
    private static String USERNAME;
    private User user;
    private FileChooser fileChooser;
    private File file;
    private String path;
    private Image image;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView imageView;

    @FXML
    private HTMLEditor editor;

    @FXML
    public void apply() throws IOException {
        Post p = new Post();
        p.setContent(editor.getHtmlText());
        p.setImagePath(path);
        p.setUser(user);
        SqlManager.getInstance().addPost(p);
        back();
    }
    @FXML
    public void back() throws IOException {
        PersonalPageController.setUsername(USERNAME);
        PageSwitcher.switchToPage("personalPage.fxml");
    }

    @FXML
    public void handleBrowser() {
        stage = (Stage) anchorPane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath().replace("\\", "/");
        } else {
            file = new File(path);
        }
        image = new Image(file.getAbsoluteFile().toURI().toString());
        imageView.setImage(image);
    }

    public static void setUsername(String username) {
        USERNAME = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = SqlManager.getInstance().findUser(USERNAME);
        path = "C:/Users/Asus/IdeaProjects/instagram/src/main/resources/com/example/instagram/images/insta_post.png";
        try {
            imageView.setImage(new Image(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif", "*.jfif")
//                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
    }
}
