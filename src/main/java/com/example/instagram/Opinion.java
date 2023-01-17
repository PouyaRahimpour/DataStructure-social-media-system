package com.example.instagram;

import com.example.instagram.models.Post;
import com.example.instagram.models.User;

public class Opinion {
    private User user;
    private String state;
    private Post post;
    public Opinion() {}

    public Opinion(String state, User user, Post post) {
        this.post = post;
        this.user = user;
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public String getState() {
        return state;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setState(String state) {
        this.state = state;
    }
}
