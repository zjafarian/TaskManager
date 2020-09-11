package com.example.taskmanager.repository;

import com.example.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

public interface IRepositoryUser {
    List<User> getUserList();
    User getUser(UUID userId);
    void insertUser(User user);
    void deleteUser(User user);
    int getPosition(UUID userId);

}
