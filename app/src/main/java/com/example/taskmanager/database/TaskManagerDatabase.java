package com.example.taskmanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

@Database(entities = {Task.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class TaskManagerDatabase extends RoomDatabase {

    public abstract TaskDAO getTaskTable();

    public abstract UserDAO getUserTable();


}
