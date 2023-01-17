package com.example.instagram;

import com.example.instagram.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class EditProfilePageController implements Initializable {
    private FileChooser fileChooser;
    private static User user;
    private static String USERNAME;
    private File file;
    private Stage stage;
    private Image image;
    private FileInputStream fis;
    private String path;
    private String[] states = {"public", "private"};

    @FXML
    private ImageView imageView;
    @FXML
    private TextField mobileNumber, fullName;
    @FXML
    private TextArea bio;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker birthday;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnBrowser;
    @FXML
    private ChoiceBox pageState;

    @FXML
    public void handleBrowser() {
        stage = (Stage) anchorPane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath().replace("\\", "/");
        } else {
            path = "C:/Users/Asus/IdeaProjects/instagram/src/main/resources/com/example/instagram/images/user_fill.png";
            file = new File(path);
        }
        image = new Image(file.getAbsoluteFile().toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    public void changeInfo() throws IOException {
        User u = new User(user.getUsername(), password.getText(), fullName.getText(), mobileNumber.getText());
//        String searched_date = birthday.getValue().toString();
//        System.out.println(searched_date);
//        Date date = Date.valueOf(searched_date);
        String date = birthday.getValue()==null? "":birthday.getValue().toString();
        String state = (String) pageState.getValue();
        u.setPageState(state);
        SqlManager.getInstance().updateUserInfo(u, bio.getText(), path, date);
        PersonalPageController.setUsername(USERNAME);
        PageSwitcher.switchToPage("personalPage.fxml");
    }

    public void back() throws IOException {
        PersonalPageController.setUsername(user.getUsername());
        PageSwitcher.switchToPage("personalPage.fxml");
    }



    public static void setUsername(String  u) {
            USERNAME = u;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = SqlManager.getInstance().findUser(USERNAME);
        try {
            imageView.setImage(new Image(new FileInputStream(user.getProfImagePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mobileNumber.setText(user.getMobileNumber());
        password.setText(user.getPassword());
        fullName.setText(user.getFullName());
        path = user.getProfImagePath();
        fileChooser = new FileChooser();
        birthday.setAccessibleText(user.getBirthday());
        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
//                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
        pageState.setValue(user.getPageState());
        pageState.getItems().addAll(states);
    }
}
