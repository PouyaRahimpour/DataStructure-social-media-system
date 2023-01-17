package com.example.instagram.models;

public class Handle {
    public static String currentDate() {
        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        return date.toString();
    }

}
