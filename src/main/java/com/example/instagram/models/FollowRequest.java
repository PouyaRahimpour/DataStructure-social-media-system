package com.example.instagram.models;

public class FollowRequest {
    private boolean accepted;
    private User follower;
    private User following;
    private String time;

    public FollowRequest() {}
    public FollowRequest(User follower, User following, String time) {
        this.follower = follower;
        this.following = following;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowing() {
        return following;
    }
}
