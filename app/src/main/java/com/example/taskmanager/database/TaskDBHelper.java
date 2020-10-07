package com.example.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taskmanager.database.TaskManagerSchema.TaskTable.taskCols;
import com.example.taskmanager.database.TaskManagerSchema.UserTable.userCols;
public class TaskDBHelper extends SQLiteOpenHelper {


    public TaskDBHelper(@Nullable Context context) {
        super(context, TaskManagerSchema.NAME , null, TaskManagerSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder userQuery = new StringBuilder();
        userQuery.append("CREATE TABLE " + TaskManagerSchema.UserTable.NAME + " (");
        userQuery.append(userCols.id + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        userQuery.append(userCols.userUUID + " TEXT NOT NULL,");
        userQuery.append(userCols.userName + " TEXT,");
        userQuery.append(userCols.password + " TEXT");
        userQuery.append(");");

        StringBuilder tableQuery = new StringBuilder();

        tableQuery.append("CREATE TABLE " + TaskManagerSchema.TaskTable.NAME + " (");
        tableQuery.append(taskCols.idTask + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        tableQuery.append(taskCols.idUser + " INTEGER FOREIGN KEY FROM userTable,");
        tableQuery.append(taskCols.taskUUID+ " TEXT NOT NULL,");
        tableQuery.append(taskCols.userUUID+ " TEXT NOT NULL,");
        tableQuery.append(taskCols.title + " TEXT,");
        tableQuery.append(taskCols.description + " TEXT,");
        tableQuery.append(taskCols.date + " TEXT,");
        tableQuery.append(taskCols.state + " TEXT");
        tableQuery.append(");");


        db.execSQL(userQuery.toString());
        db.execSQL(tableQuery.toString());
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
