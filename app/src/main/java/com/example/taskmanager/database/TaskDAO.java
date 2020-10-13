package com.example.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.model.Task;

import java.util.List;
import java.util.UUID;

@Dao
public interface TaskDAO {
    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM TaskTable")
    List<Task> getTaskList();

    @Query("SELECT * FROM TaskTable WHERE uuidTask=:id")
    Task getTask(UUID id);

   /* @Query("SELECT * FROM TaskTable WHERE uuidUser =:idUser")
    Task getTaskUser(UUID idUser);*/


}
