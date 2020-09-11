package com.example.taskmanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.zip.DataFormatException;

public class Task implements Serializable {
    private UUID mIdTask;
    private String mTitleTask;
    private String mDescription;
    private State mStateTask;
    private UUID mIdUser;
    private Date mDateTask;



    public Task(String title, State stateTask, String description, User  user) {
        mIdTask = UUID.randomUUID();
        mIdUser = user.getIDUser();
        mTitleTask = title;
        mStateTask = stateTask;
        mDescription = description;
    }

    public State getStateTask() {
        return mStateTask;
    }

    public void setStateTask(State stateTask) {
        mStateTask = stateTask;
    }

    public Date getDateTask() {
        return mDateTask;
    }

    public void setDateTask(Date dateTask) {
        mDateTask = dateTask;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public UUID getIdUser() {
        return mIdUser;
    }

    public void setIdUser(UUID idUser) {
        mIdUser = idUser;
    }

    public UUID getIdTask() {
        return mIdTask;
    }

    public void setIdTask(UUID idTask) {
        mIdTask = idTask;
    }

    public String getTitleTask() {
        return mTitleTask;
    }

    public void setTitleTask(String titleTask) {
        mTitleTask = titleTask;
    }
}