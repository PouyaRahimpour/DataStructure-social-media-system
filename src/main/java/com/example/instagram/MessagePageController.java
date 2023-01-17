package com.example.instagram;

import com.example.instagram.models.Comment;
import com.example.instagram.models.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MessagePageController implements Initializable {
    private ArrayList<Message> messages;
    private ArrayList<Message> fromUser;
    private ArrayList<Message> toUser;


    private static User user;
    private Pane currentPane;

    @FXML
    private Button apply;

    @FXML
    private ImageView frontImage, profImage;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ListView<Pane> inboxListView, outboxListView;

    @FXML
    void apply() throws IOException, InterruptedException {
        if (!messageTextArea.getText().equals("")) {
            User to = SqlManager.getInstance().findUser(usernameTextField.getText());
            SqlManager.getInstance().addMessage(new Message(user, to, messageTextArea.getText()));
        }
        MessagePageController.setUser(user);
        PageSwitcher.switchToPage("messagePage.fxml");
    }

    @FXML
    void back() throws IOException {
        PersonalPageController.setUsername(user.getUsername());
        PageSwitcher.switchToPage("personalPage.fxml");
    }

    public void showInbox() {
        while (inboxListView.getItems().size() > 0) inboxListView.getItems().remove(0);
        for (Message m: toUser) {
            User u = m.getFrom();
            Pane pane = new Pane();
            pane.setPrefSize(350, 80);
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
            l.setText(m.getMessage());
            l.setPrefSize(280, 80);
            l.setTranslateX(80);
            l.setFont(Font.font("Lucida Fax", 10));
            l.setTranslateY(0);
            l.setAlignment(Pos.TOP_LEFT);
            pane.getChildren().add(l);
            inboxListView.getItems().add(pane);
        }
        inboxListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = inboxListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void showOutbox() {
        while (outboxListView.getItems().size() > 0) outboxListView.getItems().remove(0);
        for (Message m: fromUser) {
            User u = m.getTo();
            Pane pane = new Pane();
            pane.setPrefSize(350, 80);
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
            l.setText(m.getMessage());
            l.setPrefSize(280, 80);
            l.setTranslateX(80);
            l.setFont(Font.font("Lucida Fax", 10));
            l.setTranslateY(0);
            l.setAlignment(Pos.TOP_LEFT);
            pane.getChildren().add(l);
            outboxListView.getItems().add(pane);
        }
        outboxListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pane>() {
            @Override
            public void changed(ObservableValue<? extends Pane> observableValue, Pane webView, Pane t1) {
                currentPane = outboxListView.getSelectionModel().getSelectedItem();
            }
        });
    }

    public static void setUser(User user) {
        MessagePageController.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messages = SqlManager.getInstance().getMessages();
        fromUser = new ArrayList<>();
        toUser = new ArrayList<>();
        for (Message m: messages) if (m.getTo().equals(user)) toUser.add(m);
        for (Message m: messages) if (m.getFrom().equals(user)) fromUser.add(m);
        showInbox();
        showOutbox();

         Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String us = usernameTextField.getText();
                User u = SqlManager.getInstance().findUser(us);
                if (u.getUsername() != null) {
                    try {
                        profImage.setImage(new Image(new FileInputStream(u.getProfImagePath())));
                        profImage.setOnMouseClicked(mouseEvent -> {
                            try {
                                search(u.getUsername());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    apply.setVisible(true);
                } else {
                    String path = "C:/Users/Asus/IdeaProjects/instagram/src/main/resources/com/example/instagram/images/insta_post.png";
                    try {
                        profImage.setImage(new Image(new FileInputStream(path)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    apply.setVisible(false);
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void search(String username) throws IOException {
        SearchPageController.setUser(user.getUsername());
        SearchPageController.setAutoSearchUsername(username);
        PageSwitcher.switchToPage("searchPage.fxml");
    }
}
