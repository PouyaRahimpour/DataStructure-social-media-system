package com.example.instagram;

import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;

public class PersonalPageController implements Initializable {
    private ArrayList<Post> posts;
    private ArrayList<String> followings, followers, antiFollowers, advocates, mutualFollowers;

    private Pane currentPane;

    @FXML
    private Label username, fullName, postsN, followingsN, followersN, bio, advocatesN, antiFollowersN, mutualFollowersN;
    @FXML
    private ImageView profImage;
    @FXML
    private ListView<Pane> postsListView, followersListView, followingsListView, advocatesListView, antiFollowersListView, mutualFollowersListView;
    @FXML
    private ImageView requests;

    private static User user;
    private static String USERNAME;

    public void editProfile() throws IOException {
        EditProfilePageController.setUsername(user.getUsername());
        PageSwitcher.switchToPage("editProfilePage.fxml");
    }

    public void show() {
    }

    public void entered() {
    }

    public void exited() {
    }

    public void back() throws IOException {
        PageSwitcher.switchToPage("logInPage.fxml");
    }

    public void search(String username) throws IOException {
        SearchPageController.setUser(USERNAME);
        SearchPageController.setAutoSearchUsername(username);
        PageSwitcher.switchToPage("searchPage.fxml");
    }

    public void switchToSearchPage() throws IOException {
        SearchPageController.setUser(USERNAME);
        SearchPageController.setAutoSearch(false);
        PageSwitcher.switchToPage("searchPage.fxml");
    }

    public void addPost() throws IOException {
        AddPostPageController.setUsername(user.getUsername());
        PageSwitcher.switchToPage("addPostPage.fxml");
    }

    public static void setUsername(String u) {
        USERNAME = u;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = SqlManager.getInstance().findUser(USERNAME);
        posts = SqlManager.getInstance().findPosts(USERNAME);
        followers = SqlManager.getInstance().getFollowers(USERNAME);
        followings = SqlManager.getInstance().getFollowings(USERNAME);
        advocates = new ArrayList<>();
        for (String s : followers) if (!followings.contains(s)) advocates.add(s);
        antiFollowers = new ArrayList<>();
        for (String s : followings) if (!followers.contains(s)) antiFollowers.add(s);
        mutualFollowers = new ArrayList<>();
        for (String s : followers) if (followings.contains(s)) mutualFollowers.add(s);

        postsN.setText(String.valueOf(posts.size()));
        followersN.setText(String.valueOf(followers.size()));
        followingsN.setText(String.valueOf(followings.size()));
        advocatesN.setText(String.valueOf(advocates.size()));
        antiFollowersN.setText(String.valueOf(antiFollowers.size()));
        mutualFollowersN.setText(String.valueOf(mutualFollowers.size()));


        bio.setText(user.getBio());
        try {
            profImage.setImage(new Image(new FileInputStream(user.getProfImagePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        username.setText(user.getUsername());
        fullName.setText(user.getFullName());
        visibleMenu(false);
        showPosts();
    }

    public void showFollowers() {
        while (followersListView.getItems().size() > 0) followersListView.getItems().remove(0);
        visibleMenu(false);
        followersListView.setVisible(true);
        for (String s : followers) {
            User u = SqlManager.getInstance().findUser(s);

            Pane pane = new Pane();
            pane.setPrefSize(380, 80);
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
                        search(s);
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
            l.setText(s);
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            followersListView.getItems().add(pane);
        }
        followersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = followersListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void showFollowings() {
        while (followingsListView.getItems().size() > 0) followingsListView.getItems().remove(0);
        visibleMenu(false);
        followingsListView.setVisible(true);
        for (String s : followings) {
            User u = SqlManager.getInstance().findUser(s);

            Pane pane = new Pane();
            pane.setPrefSize(380, 80);
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
                        search(s);
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
            l.setText(s);
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setFont(Font.font("Lucida Fax", 13));
            l.setTranslateY(0);
            pane.getChildren().add(l);
            followingsListView.getItems().add(pane);
        }
        followingsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = followingsListView.getSelectionModel().getSelectedItem();
            }
        });

    }

    public void showPosts() {
        while (postsListView.getItems().size() > 0) postsListView.getItems().remove(0);
        visibleMenu(false);
        postsListView.setVisible(true);
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
                    editPost(p);
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
            postsListView.getItems().add(pane);
        }
        postsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = postsListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void showAdvocates() {
        while (advocatesListView.getItems().size() > 0) advocatesListView.getItems().remove(0);
        visibleMenu(false);
        advocatesListView.setVisible(true);
        for (String s : advocates) {
            User u = SqlManager.getInstance().findUser(s);

            Pane pane = new Pane();
            pane.setPrefSize(380, 80);
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
                        search(s);
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
            l.setText(s);
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            advocatesListView.getItems().add(pane);
        }
        advocatesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = advocatesListView.getSelectionModel().getSelectedItem();
            }
        });

    }

    public void showAntiFollowers() {
        while (antiFollowersListView.getItems().size() > 0) antiFollowersListView.getItems().remove(0);
        visibleMenu(false);
        antiFollowersListView.setVisible(true);
        for (String s : antiFollowers) {
            User u = SqlManager.getInstance().findUser(s);

            Pane pane = new Pane();
            pane.setPrefSize(380, 80);
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
                        search(s);
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
            l.setText(s);
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            antiFollowersListView.getItems().add(pane);
        }
        antiFollowersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = antiFollowersListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void showMutualFollowers() {
        while (mutualFollowersListView.getItems().size() > 0) mutualFollowersListView.getItems().remove(0);
        visibleMenu(false);
        mutualFollowersListView.setVisible(true);
        for (String s : mutualFollowers) {
            User u = SqlManager.getInstance().findUser(s);

            Pane pane = new Pane();
            pane.setPrefSize(380, 80);
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
                        search(s);
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
            l.setText(s);
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            mutualFollowersListView.getItems().add(pane);
        }
        mutualFollowersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = mutualFollowersListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    private void visibleMenu(boolean b) {
        postsListView.setVisible(b);
        followersListView.setVisible(b);
        followingsListView.setVisible(b);
        advocatesListView.setVisible(b);
        antiFollowersListView.setVisible(b);
        mutualFollowersListView.setVisible(b);
    }

    public void switchToRequestsPage() throws IOException {
        RequestsPageController.setUsername(USERNAME);
        PageSwitcher.switchToPage("requestsPage.fxml");
    }

    public void switchToMessagePage() throws IOException {
        MessagePageController.setUser(user);
        PageSwitcher.switchToPage("messagePage.fxml");
    }

    public void editPost(Post post) {
        try {
            EditPostPageController.setUser(user);
            EditPostPageController.setPost(post);
            PageSwitcher.switchToPage("editPostPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
