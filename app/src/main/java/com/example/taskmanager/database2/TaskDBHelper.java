package com.example.taskmanager.database2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class TaskDBHelper extends SQLiteOpenHelper {


    public TaskDBHelper(@Nullable Context context) {
        super(context, TaskManagerSchema.NAME , null, TaskManagerSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder userQuery = new StringBuilder();
        userQuery.append("CREATE TABLE " + TaskManagerSchema.UserTable.NAME + " (");
        userQuery.append(TaskManagerSchema.UserTable.userCols.id + " " +
                "INTEGER PRIMARY KEY AUTOINCREMENT,");
        userQuery.append(TaskManagerSchema.UserTable.userCols.userUUID + " TEXT NOT NULL,");
        userQuery.append(TaskManagerSchema.UserTable.userCols.userName + " TEXT,");
        userQuery.append(TaskManagerSchema.UserTable.userCols.password + " TEXT");
        userQuery.append(");");

        StringBuilder tableQuery = new StringBuilder();

        tableQuery.append("CREATE TABLE " + TaskManagerSchema.TaskTable.NAME + " (");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.idTask +
                " INTEGER PRIMARY KEY AUTOINCREMENT,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.idUser +
                " INTEGER FOREIGN KEY FROM userTable,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.taskUUID+ " TEXT NOT NULL,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.userUUID+ " TEXT NOT NULL,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.title + " TEXT,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.description + " TEXT,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.date + " TEXT,");
        tableQuery.append(TaskManagerSchema.TaskTable.taskCols.state + " TEXT");
        tableQuery.append(");");


        db.execSQL(userQuery.toString());
        db.execSQL(tableQuery.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
