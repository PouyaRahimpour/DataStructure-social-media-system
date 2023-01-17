package com.example.instagram.models;

public class Comment {
    User user;
    String content;
    Post post;
    String time;

    public Comment() {}

    public Comment(User user, String content, Post post, String time) {
        this.user = user;
        this.content = content;
        this.post = post;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
