package com.example.foodloopapp.model;

public class User {

    private String uid;
    private String email;
    private String fullName;
    private long createdAt;   // epoch millis

    // Required empty constructor for Firestore
    public User() {
    }

    public User(String uid, String email, String fullName, long createdAt) {
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
