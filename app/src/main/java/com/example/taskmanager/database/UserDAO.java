package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(User user);

    @Update

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM UserTable")
    List<User> getUserList();

    @Query("SELECT * FROM UserTable WHERE uuidUser=:userId")
    User getUser(UUID userId);


}
