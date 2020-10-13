package com.example.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@Entity(tableName = "TaskTable")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTask")
    private int mId;

    @ColumnInfo(name = "uuidTask")
    private UUID mIdTask;

    @ColumnInfo(name = "titleTask")
    private String mTitleTask = "title";

    @ColumnInfo(name = "descriptionTask")
    private String mDescription = "description";

    @ColumnInfo(name = "stateTask")
    private State mStateTask = State.Todo;

    @ColumnInfo(name = "idUser")
    private int mUserId;

    @ColumnInfo(name = "uuidUser")
    private UUID mIdUser;

    @ColumnInfo(name = "dateTask")
    private Date mDateTask = new Date();





    public Task(String title, String description, State stateTask, UUID idUser) {
        mIdTask = UUID.randomUUID();
        mIdUser = idUser;
        mTitleTask = title;
        mStateTask = stateTask;
        mDescription = description;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
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

    private State generateRandomState() {
        State[] states = State.values();
        int length = states.length - 1;
        int rndIndex = (int) (Math.random() * (length - 0 + 1) + 0);
        return states[rndIndex];
    }
}
