package com.example.instagram;

import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditPostPageController implements Initializable {
    private ArrayList<Comment> comments;
    private ArrayList<Opinion> likes, dislikes;
    private static User user;
    private static Post post;
    private Pane currentPane;

    @FXML
    private ImageView commentImageView, dislikeImageView, likeImageView;

    @FXML
    private ListView<Pane> commentsListView, likesListView, dislikesListView;

    @FXML
    private Label dislikesLabel, likesLabel, commentsLabel;

    @FXML
    void back() throws IOException {
        PersonalPageController.setUsername(user.getUsername());
        PageSwitcher.switchToPage("personalPage.fxml");
    }

    @FXML
    public void showComments() {
        while (commentsListView.getItems().size() > 0) commentsListView.getItems().remove(0);
        visibleMenu(false);
        commentsListView.setVisible(true);
        for (Comment c : comments) {
            User u = c.getUser();

            Pane pane = new Pane();
            pane.setPrefSize(300, 80);
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
                        search(u.getUsername());
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
            l.setText(c.getContent());
            l.setPrefSize(220, 80);
            l.setTranslateX(80);
            l.setFont(Font.font("Lucida Fax", 10));
            l.setTranslateY(0);
            l.setAlignment(Pos.TOP_LEFT);
            pane.getChildren().add(l);

            Button b1 = new Button();
            b1.setText("Delete");
            b1.setOnAction(event -> {
                deleteComment(c);
            });

            b1.setPrefSize(60, 20);
            b1.setTranslateX(230);
            b1.setTranslateY(50);
            pane.getChildren().add(b1);


            commentsListView.getItems().add(pane);
        }

        commentsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = commentsListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    @FXML
    void showDislikes() {
        while (dislikesListView.getItems().size() > 0) dislikesListView.getItems().remove(0);
        visibleMenu(false);
        dislikesListView.setVisible(true);
        for (Opinion o : dislikes) {
            User u = o.getUser();
            // 300 460
            Pane pane = new Pane();
            pane.setPrefSize(300, 80);
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
                        search(u.getUsername());
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
            l.setText(u.getUsername());
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            dislikesListView.getItems().add(pane);
        }
        dislikesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = dislikesListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    @FXML
    void showLikes() {
        while (likesListView.getItems().size() > 0) likesListView.getItems().remove(0);
        visibleMenu(false);
        likesListView.setVisible(true);
        for (Opinion o : likes) {
            User u = o.getUser();
            // 300 460
            Pane pane = new Pane();
            pane.setPrefSize(300, 80);
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
                        search(u.getUsername());
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
            l.setText(u.getUsername());
            l.setFont(Font.font("Lucida Fax", 13));
            l.setPrefSize(80, 80);
            l.setTranslateX(100);
            l.setTranslateY(0);
            pane.getChildren().add(l);
            likesListView.getItems().add(pane);
        }
        likesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = likesListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void search(String username) throws IOException {
        SearchPageController.setUser(user.getUsername());
        SearchPageController.setAutoSearchUsername(username);
        PageSwitcher.switchToPage("searchPage.fxml");
    }

    public void deleteComment(Comment c) {
        SqlManager.getInstance().deleteComment(c);
        try {
            EditPostPageController.setPost(post);
            EditPostPageController.setUser(user);
            PageSwitcher.switchToPage("editPostPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUser(User user) {
        EditPostPageController.user = user;
    }

    public static void setPost(Post post) {
        EditPostPageController.post = post;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        likes = SqlManager.getInstance().getLikes(post);
        dislikes = SqlManager.getInstance().getDislikes(post);
        comments = SqlManager.getInstance().getComments(post);
        likesLabel.setText(String.valueOf(likes.size()));
        dislikesLabel.setText(String.valueOf(dislikes.size()));
        commentsLabel.setText(String.valueOf(comments.size()));
    }

    public void visibleMenu(boolean b) {
        commentsListView.setVisible(b);
        likesListView.setVisible(b);
        dislikesListView.setVisible(b);
    }
}
