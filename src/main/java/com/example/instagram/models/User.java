package com.example.instagram.models;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String mobileNumber;
    private String pageState;

    private String bio;
    private String birthday;
    private String profImagePath;

    private void firstSetUP() {
        bio = "Hey, welcome to instagram!\nThis is your biography...\nYou can change it whenever you want";
        profImagePath = "C:\\Users\\Asus\\IdeaProjects\\instagram\\src\\main\\resources\\com\\example\\instagram\\images\\user_fill.png";
        pageState = "public";
    }

    public User() {
        firstSetUP();
    }

    public User(String username, String password, String fullName, String mobileNumber) {
        firstSetUP();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPageState(String pageState) {
        this.pageState = pageState;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPageState() {
        return pageState;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfImagePath() {
        return profImagePath;
    }

    public void setProfImagePath(String profImage) {
        this.profImagePath = profImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(fullName, user.fullName) && Objects.equals(mobileNumber, user.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, fullName, mobileNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }


    public void copy(User user) {
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.mobileNumber = user.getMobileNumber();
    }
}
