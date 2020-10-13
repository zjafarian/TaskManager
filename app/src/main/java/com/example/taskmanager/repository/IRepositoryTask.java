package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public interface IRepositoryTask extends Serializable {
    void insertTask (Task task);
    Task getTask (UUID id);
    void updateTask (Task task);
    void deleteTask (Task task);
    int getPosition (UUID taskId);
    List<Task> getTaskList();
    Task getTaskUser (UUID idUser);
}
