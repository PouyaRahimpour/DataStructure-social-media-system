package com.example.instagram;

import com.example.instagram.models.FollowRequest;
import com.example.instagram.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RequestsPageController implements Initializable {
    private static String USERNAME;
    private User user;
    private Pane currentPane;
    private ArrayList<FollowRequest> requests;


    @FXML
    private ListView<Pane> requestsListView;

    public void showRequests() {

        for (FollowRequest request : requests) {
            User u = request.getFollower();

            Pane pane = new Pane();
            pane.setPrefSize(430, 80);
            pane.setOpacity(0.8);
            pane.setOnMouseEntered(mouseEvent -> {
                pane.setOpacity(1);
            });
            pane.setOnMouseExited(mouseEvent -> {
                pane.setOpacity(0.8);
            });
            try {
                ImageView iv = new ImageView(new Image(new FileInputStream(u.getProfImagePath())));
                iv.setOnMouseClicked(mouseEvent -> {
                    try {
                        search(request.getFollower().getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                iv.setFitWidth(80);
                iv.setFitHeight(80);
                pane.getChildren().add(iv);
                iv.setTranslateX(0);
                iv.setTranslateY(0);
                ImageView iv2 = new ImageView(new Image(new FileInputStream(
                        "C:\\Users\\Asus\\IdeaProjects\\instagram\\src\\main" +
                                "\\resources\\com\\example\\instagram\\images\\circle4.png")));
                iv2.setFitWidth(80);
                iv2.setFitHeight(80);
                pane.getChildren().add(iv2);
                iv2.setTranslateX(0);
                iv2.setTranslateY(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Label l = new Label();
            l.setText(request.getFollower().getUsername());
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(50, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);

            Label ll = new Label();
            ll.setText(request.getTime());
            ll.setFont(Font.font("Lucida Fax", 10));
            ll.setPrefSize(200, 80);
            ll.setTranslateX(160);
            ll.setTranslateY(0);
            pane.getChildren().add(ll);

            Button b1 = new Button();
            b1.setText("Accept");
            Button b2 = new Button();
            b2.setText("Reject");
            b1.setOnAction(event -> {
                accept(request.getFollower().getUsername());
            });
            b2.setOnAction(event -> {
                reject(request.getFollower().getUsername());
            });
            b1.setPrefSize(80, 20);
            b2.setPrefSize(80, 20);
            b1.setTranslateX(330);
            b1.setTranslateY(15);
            b2.setTranslateX(330);
            b2.setTranslateY(45);
            pane.getChildren().add(b1);
            pane.getChildren().add(b2);

            requestsListView.getItems().add(pane);
        }
        requestsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = requestsListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void accept(String username) {
        SqlManager.getInstance().acceptRequest(username, user.getUsername());
        try {
            RequestsPageController.setUsername(USERNAME);
            PageSwitcher.switchToPage("requestsPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reject(String username) {
        SqlManager.getInstance().rejectRequest(username, user.getUsername());
        try {
            RequestsPageController.setUsername(USERNAME);
            PageSwitcher.switchToPage("requestsPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void setUsername(String u) {
        USERNAME = u;
    }

    public void back() throws IOException {
        PersonalPageController.setUsername(USERNAME);
        PageSwitcher.switchToPage("personalPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = SqlManager.getInstance().findUser(USERNAME);
        requests = SqlManager.getInstance().getFollowRequestsFollowersOf(USERNAME);
        showRequests();
    }

    public void search(String username) throws IOException {
        SearchPageController.setUser(USERNAME);
        SearchPageController.setAutoSearchUsername(username);
        PageSwitcher.switchToPage("searchPage.fxml");
    }
}
