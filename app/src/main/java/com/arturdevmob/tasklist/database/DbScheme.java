package com.arturdevmob.tasklist.database;

public class DbScheme {
    public static final String DATABASE_NAME = "task_list";
    public static final int DATABASE_VERSION = 1;

    public static class Task {
        public static final String TABLE_NAME = "task";

        public static class COLUMN {
            public static final String ID = "_id";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String CREATION_DATE = "creation_date";
            public static final String PRIORITY_ID = "priority";
            public static final String DEADLINE_DATE = "deadline_date";
            public static final String DONE = "done";
        }
    }

    public static class TaskPriority {
        public static final String TABLE_NAME = "task_priority";

        public static class COLUMN {
            public static final String PRIORITY_ID = "priority_id";
            public static final String TITLE_RES = "title_res";
            public static final String COLOR_RES = "color_res";
        }
    }
}
