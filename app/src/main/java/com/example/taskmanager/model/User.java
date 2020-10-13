package com.example.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@Entity (tableName = "UserTable")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUser")
    private int mId;

    @ColumnInfo(name = "userName")
    private String mUsername;

    @ColumnInfo (name = "password")
    private String mPassword;

    @ColumnInfo(name = "uuidUser")
    private UUID mIDUser;

    @ColumnInfo(name ="dateSignUp")
    private Date mDate;

    public User() {
    }

    public User(String username, String password) {
        mIDUser = UUID.randomUUID();
        mUsername = username;
        mPassword = password;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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
