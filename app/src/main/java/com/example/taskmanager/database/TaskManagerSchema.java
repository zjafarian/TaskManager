package com.example.taskmanager.database;

public class TaskManagerSchema {
    public static final String NAME = "taskManager.db";
    public static final int VERSION = 1;

    public static final class UserTable {
        public static final String NAME = "userTable";

        public static final class userCols {
            public static final String id = "idUser";
            public static final String userUUID = "uuidUser";
            public static final String userName = "userName";
            public static final String password = "password";

        }
    }

    public static final class TaskTable {
        public static final String NAME = "taskTable";

        public static final class taskCols {
            public static final String idUser = "userId";
            public static final String idTask = "taskId";
            public static final String taskUUID = "uuidTask";
            public static final String userUUID = "uuidUser";
            public static final String title = "titleTask";
            public static final String description = "descriptionTask";
            public static final String date = "dateTask";
            public static final String state = "stateTask";
        }
    }
}
