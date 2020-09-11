package com.example.taskmanager.model;

import java.util.UUID;

public class User {
    private String mUsername;
    private String mPassword;
    private UUID mIDUser;

    public User(String username, String password) {
        mIDUser = UUID.randomUUID();
        mUsername = username;
        mPassword = password;
    }


    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public UUID getIDUser() {
        return mIDUser;
    }

    public void setIDUser(UUID IDUser) {
        mIDUser = IDUser;
    }


}
