package com.example.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.room.Room;

import com.example.taskmanager.database.TaskDAO;
import com.example.taskmanager.database.TaskManagerDatabase;
import com.example.taskmanager.database.TaskManagerSchema;
import com.example.taskmanager.database.TaskManagerSchema.TaskTable.taskCols;
import com.example.taskmanager.database.TaskManagerSchema.UserTable.userCols;
import com.example.taskmanager.database.UserDAO;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class TaskManagerDBRepository implements IRepositoryUser, IRepositoryTask {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static TaskManagerDBRepository sInstance;

    private TaskDAO mTaskDAO;
    private UserDAO mUserDAO;


    public TaskManagerDBRepository(Context context) {
        mContext = context.getApplicationContext();
       /* TaskDBHelper taskDBHelper = new TaskDBHelper(mContext);
        mDatabase = taskDBHelper.getWritableDatabase();*/
        TaskManagerDatabase taskManagerDatabase = Room.databaseBuilder(mContext,
                TaskManagerDatabase.class, "taskManager.db")
                .allowMainThreadQueries()
                .build();

        mTaskDAO = taskManagerDatabase.getTaskTable();
        mUserDAO = taskManagerDatabase.getUserTable();
    }

    public static TaskManagerDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskManagerDBRepository(context);

        return sInstance;
    }

    @Override
    public void insertTask(Task task) {
       /* ContentValues values = getContentTaskValues(task);
        mDatabase.insert(TaskManagerSchema.TaskTable.NAME,null,values);*/
        mTaskDAO.insertTask(task);
    }

    @Override
    public Task getTask(UUID id) {
      /*  String where = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{id.toString()};

        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(where, whereArgs);

        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return null;

        try {
            taskCursorWrapper.moveToFirst();
            return taskCursorWrapper.getTask();
        } finally {
            taskCursorWrapper.close();
        }*/

        return mTaskDAO.getTask(id);
    }

    @Override
    public void updateTask(Task task) {
       /* ContentValues values = getContentTaskValues(task);
        String whereClause = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{task.getIdTask().toString()};
        mDatabase.update(TaskManagerSchema.TaskTable.NAME,values,whereClause,whereArgs);*/

        mTaskDAO.updateTask(task);
    }

    @Override
    public void deleteTask(Task task) {
       /* String whereClause = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{task.getIdTask().toString()};
        mDatabase.delete(TaskManagerSchema.TaskTable.NAME,whereClause,whereArgs);*/

        mTaskDAO.deleteTask(task);
    }

    @Override
    public List<Task> getTaskList() {
     /*   List<Task> tasks = new ArrayList<>();
        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(null, null);
        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return tasks;

        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                tasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }

        } finally {
            taskCursorWrapper.close();
        }*/
        return mTaskDAO.getTaskList();
    }

    @Override
    public Task getTaskUser(UUID idUser) {
        /*return mTaskDAO.getTaskUser(idUser);*/
        return null;
    }

    @Override
    public File getPhotoFile(Task task) {

        // /data/data/com.example.taskmanager/files/
        File filesDir = mContext.getFilesDir();

        // /data/data/com.example.taskmanager/files/IMG_ktui4u544nmkfuy48485.jpg


        File photoFile = new File(filesDir, task.getFilePhoto());
        if (photoFile == null){
            Uri uriPhoto = task.getUri();
            photoFile = new File(uriPhoto.getPath());
        }


        return photoFile;
    }

    @Override
    public List<User> getUserList() {
       /* List<User> users = new ArrayList<>();
        UserCursorWrapper userCursorWrapper  = queryUserCursor(null, null);
        if (userCursorWrapper == null || userCursorWrapper.getCount() == 0)
            return users;

        try {
            userCursorWrapper.moveToFirst();
            while (!userCursorWrapper.isAfterLast()) {
                users.add(userCursorWrapper.getUser());

                userCursorWrapper.moveToNext();
            }

        } finally {
            userCursorWrapper.close();
        }*/
        return mUserDAO.getUserList();
    }

    @Override
    public User getUser(UUID userId) {
  /*      String where = userCols.userUUID + " = ?";
        String[] whereArgs = new String[]{userId.toString()};

        UserCursorWrapper userCursorWrapper = queryUserCursor(where,whereArgs);


        if (userCursorWrapper == null || userCursorWrapper.getCount() == 0)
            return null;

        try {
            userCursorWrapper.moveToFirst();
            return userCursorWrapper.getUser();
        } finally {
            userCursorWrapper.close();
        }*/
        return mUserDAO.getUser(userId);
    }

    @Override
    public void insertUser(User user) {
       /* ContentValues values = getContentUserValues(user);
        mDatabase.insert(TaskManagerSchema.UserTable.NAME, null, values);*/
        mUserDAO.insertUser(user);
    }

    @Override
    public void deleteUser(User user) {
      /*  String whereClause = userCols.userUUID + " = ?";
        String[] whereArgs = new String[]{user.getIDUser().toString()};
        mDatabase.delete(TaskManagerSchema.UserTable.NAME, whereClause, whereArgs);*/
        mUserDAO.deleteUser(user);
    }

    @Override
    public int getPosition(UUID userId) {
        List<User> users = getUserList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIDUser().equals(userId))
                return i;
        }
        return -1;
    }


    private TaskCursorWrapper queryTaskCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskManagerSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        TaskCursorWrapper taskCursorWrapper = new TaskCursorWrapper(cursor);
        return taskCursorWrapper;
    }

    private UserCursorWrapper queryUserCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskManagerSchema.UserTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);
        UserCursorWrapper userCursorWrapper = new UserCursorWrapper(cursor);
        return userCursorWrapper;
    }


    private ContentValues getContentTaskValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(taskCols.taskUUID, task.getIdTask().toString());
        values.put(taskCols.userUUID, task.getIdUser().toString());
        values.put(taskCols.title, task.getTitleTask());
        values.put(taskCols.date, task.getDateTask().getTime());
        values.put(taskCols.description, task.getDescription());
        values.put(taskCols.state, task.getStateTask().toString());
        values.put(taskCols.addressImage, task.getFilePhoto());
        values.put(taskCols.uriImage,task.getUri().toString());
        return values;
    }

    private ContentValues getContentUserValues(User user) {
        ContentValues values = new ContentValues();
        values.put(userCols.userUUID, user.getIDUser().toString());
        values.put(userCols.userName, user.getUsername());
        values.put(userCols.password, user.getPassword());
        values.put(userCols.dateRegister, user.getDate().getTime());
        return values;
    }
}
