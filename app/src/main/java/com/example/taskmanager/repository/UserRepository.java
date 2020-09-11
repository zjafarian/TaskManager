package com.example.taskmanager.repository;

import com.example.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

public class UserRepository implements IRepositoryUser {
    private List<User> mUserList;
    private static UserRepository sInstance;

    private UserRepository() {
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public void setUserList(List<User> userList) {
        mUserList = userList;
    }

    public static UserRepository getInstance() {
        if (sInstance == null)
            sInstance = new UserRepository();
        return sInstance;
    }

    public static void setInstance(UserRepository instance) {
        sInstance = instance;
    }



    @Override
    public User getUser(UUID userId) {
        for (User user: mUserList) {
            if (user.getIDUser().equals(userId))
                return user;
        }
        return null;
    }

    @Override
    public void insertUser(User user) {
        mUserList.add(user);

    }

    @Override
    public void deleteUser(User user) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).getIDUser().equals(user.getIDUser())) {
                mUserList.remove(i);
                return;
            }
        }

    }

    @Override
    public int getPosition(UUID userId) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).getIDUser().equals(userId))
                return i;
        }

        return 0;
    }





}
