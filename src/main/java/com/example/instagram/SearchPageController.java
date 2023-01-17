package com.example.instagram;

import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchPageController implements Initializable {
    private static String USERNAME;
    private static boolean autoSearch;
    private static String autoSearchUsername;
    private ArrayList<Post> posts;
    @FXML
    private ListView<Pane> listView;
    @FXML
    private Button apply;
    @FXML
    private Label postLabel, followingsLabel, followersLabel;

    private User user;
    private User user2;
    private Pane currentPane;
    private boolean follows, isPublic;

    @FXML
    private Label postsN, followingsN, followersN, bio;
    @FXML
    private Label label;
    @FXML
    private TextField username;

    @FXML
    private ImageView profImage;
    @FXML
    private ImageView frontImage;

    public static void setAutoSearchUsername(String autoSearchUsername) {
        SearchPageController.autoSearchUsername = autoSearchUsername;
        SearchPageController.autoSearch = true;
    }

    public void apply() {
        if (follows) unfollow();
        else if (isPublic) follow();
        else request();
        apply.setOpacity(0.3);
        apply.setVisible(false);
    }
    private void request() {
        SqlManager.getInstance().addRequest(user.getUsername(), user2.getUsername());
    }
    private void follow() {
        SqlManager.getInstance().addFollowing(user.getUsername(), user2.getUsername());
        apply.setOpacity(0.3);
        apply.setDisable(true);
    }
    private void unfollow() {
        SqlManager.getInstance().deleteFollowing(user.getUsername(), user2.getUsername());
        apply.setOpacity(0.3);
        apply.setDisable(true);
    }
    public void back() throws IOException {
        PersonalPageController.setUsername(USERNAME);
        PageSwitcher.switchToPage("personalPage.fxml");
    }


    public void search() {
        setVisible(false);
        user2 = SqlManager.getInstance().findUser(username.getText());
        follows = SqlManager.getInstance().getFollowers(user2.getUsername()).contains(user.getUsername());
        isPublic = user2.getPageState().equals("public");

        if (follows) apply.setText("Unfollow");

        if (user2.getUsername()==null||user2.getUsername().equals(user.getUsername())) {
            label.setText("Couldn't find this user.");
        } else if (!isPublic && !follows) {
            label.setText("This account is private");
            bio.setText(user2.getBio());
            try {
                profImage.setImage(new Image(new FileInputStream(user2.getProfImagePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            posts = SqlManager.getInstance().findPosts(user2.getUsername());
            followersN.setText(String.valueOf(SqlManager.getInstance().getFollowers(user2.getUsername()).size()));
            followingsN.setText(String.valueOf(SqlManager.getInstance().getFollowings(user2.getUsername()).size()));
            postsN.setText(String.valueOf(posts.size()));

            apply.setVisible(true);
            bio.setVisible(true);
            followingsN.setVisible(true);
            postsN.setVisible(true);
            followersN.setVisible(true);
            profImage.setVisible(true);
            frontImage.setVisible(true);

        } else {
            while (listView.getItems().size() > 0) listView.getItems().remove(0);
            label.setText("");
            setVisible(true);
            bio.setText(user2.getBio());
            try {
                profImage.setImage(new Image(new FileInputStream(user2.getProfImagePath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            posts = SqlManager.getInstance().findPosts(user2.getUsername());
            //follwers and followings
            followersN.setText(String.valueOf(SqlManager.getInstance().getFollowers(user2.getUsername()).size()));
            followingsN.setText(String.valueOf(SqlManager.getInstance().getFollowings(user2.getUsername()).size()));
            postsN.setText(String.valueOf(posts.size()));
            // load posts
            for (Post p : posts) {
                Pane pane = new Pane();
                pane.setPrefSize(380, 150);
                pane.setOpacity(0.8);
                pane.setOnMouseEntered(mouseEvent -> {
                    pane.setOpacity(1);
                });
                pane.setOnMouseExited(mouseEvent -> {
                    pane.setOpacity(0.8);
                });
                try {
                    ImageView iv = new ImageView(new Image(new FileInputStream(p.getImagePath())));
                    iv.setOnMouseClicked(mouseEvent -> {
                        viewPost(p);
                    });
                    iv.setFitWidth(150);
                    iv.setFitHeight(150);
                    pane.getChildren().add(iv);
                    iv.setTranslateX(0);
                    iv.setTranslateY(0);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                WebView w = new WebView();
                w.setContextMenuEnabled(false);
                w.setPrefSize(230, 150);
                w.setTranslateX(150);
                w.setTranslateY(0);
                pane.getChildren().add(w);
                WebEngine e = w.getEngine();
                e.loadContent(p.getContent());
                listView.getItems().add(pane);
            }
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
                @Override
                public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                    currentPane = listView.getSelectionModel().getSelectedItem();
                }
            });
        }
    }

    public static void setUser(String u) {
        USERNAME = u;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = SqlManager.getInstance().findUser(USERNAME);
        setVisible(false);
        if (autoSearch) {
            username.setText(autoSearchUsername);
            search();
        }
    }

    private void setVisible(boolean b) {
        profImage.setVisible(b);
        bio.setVisible(b);
        listView.setVisible(b);
        frontImage.setVisible(b);
        postsN.setVisible(b);
        followersN.setVisible(b);
        followingsN.setVisible(b);
        apply.setVisible(b);
        postLabel.setVisible(b);
        followingsLabel.setVisible(b);
        followersLabel.setVisible(b);
    }

    public void viewPost(Post p) {
        ViewPostPageController.setUser(user);
        ViewPostPageController.setPost(p);
        try {
            PageSwitcher.switchToPage("viewPostPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
