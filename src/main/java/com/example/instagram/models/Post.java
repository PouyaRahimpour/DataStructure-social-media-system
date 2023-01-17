package com.example.instagram.models;

import java.util.ArrayList;

public class Post {
    User user;
    String content;
    String imagePath;
    String time;

    private void firstSetUP() {
        imagePath = "C:/Users/Asus/IdeaProjects/instagram/src/main/resources/com/example/instagram/images/insta_post.png";
    }
    public Post() {
        firstSetUP();
    }
    public Post(String content, String imagePath, String time) {
        firstSetUP();
        this.imagePath = imagePath;
        this.content = content;
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
