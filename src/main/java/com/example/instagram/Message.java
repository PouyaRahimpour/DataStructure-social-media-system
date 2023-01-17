package com.example.instagram;

import com.example.instagram.models.User;

public class Message {
    private User from;
    private User to;
    private String message;

    public Message() {}

    public Message(User from, User to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }
}
