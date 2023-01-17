package com.example.instagram;

import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewPostPageController implements Initializable {
    private static User user;
    private static Post post;
    private ArrayList<Comment> comments;
    private ArrayList<Opinion> likes, dislikes;
    private int likeSign, dislikeSign;
    private Pane currentPane;

    @FXML
    private TextArea commentTextArea;
    @FXML
    private ImageView imageView, likeImageView, dislikeImageView, save;
    @FXML
    private WebView content;
    @FXML
    private Label likesLabel, dislikesLabel;
    @FXML
    private ListView<Pane> commentsListView;


    public static void setUser(User u) {
        user = u;
    }

    public static void setPost(Post p) {
        post = p;
    }

    public void apply() throws IOException {
        String text = commentTextArea.getText();
        if (!text.equals("")) {
            SqlManager.getInstance().addComment(user, text, post);
        }

        ViewPostPageController.setPost(post);
        ViewPostPageController.setUser(user);
        PageSwitcher.switchToPage("viewPostPage.fxml");
    }

    public void back() throws IOException {
        search(post.getUser().getUsername());
    }

    public void search(String username) throws IOException {
        SearchPageController.setUser(user.getUsername());
        SearchPageController.setAutoSearchUsername(username);
        PageSwitcher.switchToPage("searchPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        likeSign = 1;
        dislikeSign = 1;
        try {
            imageView.setImage(new Image(new FileInputStream(post.getImagePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        content.setContextMenuEnabled(false);
        WebEngine e = content.getEngine();
        e.loadContent(post.getContent());

        comments = SqlManager.getInstance().getComments(post);
        likes = SqlManager.getInstance().getLikes(post);
        dislikes = SqlManager.getInstance().getDislikes(post);

        showComments();
        likesLabel.setText(String.valueOf(likes.size()));
        dislikesLabel.setText(String.valueOf(dislikes.size()));

        for (Opinion o : likes) if (o.getUser().getUsername().equals(user.getUsername()) && o.getState().equals("like")) {
            likesLabel.setText(String.valueOf(likes.size()-1));
            like();
        }
        for (Opinion o : dislikes) if (o.getUser().getUsername().equals(user.getUsername()) && o.getState().equals("dislike")) {
            dislikesLabel.setText(String.valueOf(dislikes.size()-1));
            dislike();
        }

    }


    public void showComments() {
        while (commentsListView.getItems().size() > 0) commentsListView.getItems().remove(0);
        for (Comment c : comments) {
            User u = c.getUser();

            Pane pane = new Pane();
            pane.setPrefSize(190, 80);
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
            l.setPrefSize(110, 80);
            l.setTranslateX(80);
            l.setFont(Font.font("Lucida Fax", 10));
            l.setTranslateY(0);
            l.setAlignment(Pos.TOP_LEFT);
            pane.getChildren().add(l);
            commentsListView.getItems().add(pane);
        }
        commentsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = commentsListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void like() {
        if (likeSign > 0) {
            SqlManager.getInstance().addOpinion(new Opinion("like", user, post));
            likeImageView.setEffect(new DropShadow(5, Color.GREEN));
            dislikeImageView.setOpacity(0.5);
            dislikeImageView.setDisable(true);
        } else {
            SqlManager.getInstance().deleteOpinion(new Opinion("like", user, post));
            likeImageView.setEffect(new DropShadow());
            dislikeImageView.setOpacity(1);
            dislikeImageView.setDisable(false);
        }
        likesLabel.setText(String.valueOf(Integer.parseInt(likesLabel.getText())+likeSign));
        likeSign *= -1;
    }

    public void dislike() {
        if (dislikeSign > 0) {
            SqlManager.getInstance().addOpinion(new Opinion("dislike", user, post));
            dislikeImageView.setEffect(new DropShadow(5, Color.RED));
            likeImageView.setOpacity(0.5);
            likeImageView.setDisable(true);
        } else {
            SqlManager.getInstance().deleteOpinion(new Opinion("dislike", user, post));
            dislikeImageView.setEffect(new DropShadow());
            likeImageView.setOpacity(1);
            likeImageView.setDisable(false);
        }
        dislikesLabel.setText(String.valueOf(Integer.parseInt(dislikesLabel.getText())+dislikeSign));
        dislikeSign *= -1;
    }
}
