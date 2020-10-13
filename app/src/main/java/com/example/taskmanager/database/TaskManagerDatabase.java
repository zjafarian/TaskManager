package com.example.taskmanager.database;

import androidx.room.RoomDatabase;

public abstract class TaskManagerDatabase extends RoomDatabase {

    public abstract TaskDAO getTaskTable();

    public abstract UserDAO getUserTable();


}
