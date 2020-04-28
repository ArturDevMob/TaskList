package com.arturdevmob.tasklist.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arturdevmob.tasklist.database.DbHelper;
import com.arturdevmob.tasklist.database.DbScheme.Task;
import com.arturdevmob.tasklist.database.DbScheme.TaskPriority;
import com.arturdevmob.tasklist.utils.NotificationUtils.NotificationAlarmManager;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;

public class TaskModel {
    private static TaskModel instance;
    private SQLiteDatabase mDatabase;
    private SharedPreferences mSettingsApp;
    private Context mContextApp;

    private TaskModel(Context contextApp) {
        mContextApp = contextApp;
        this.mDatabase = new DbHelper(contextApp).getWritableDatabase();
        this.mSettingsApp = PreferenceManager.getDefaultSharedPreferences(contextApp);
    }

    public static TaskModel getInstance(Context context) {
        if (instance == null) instance = new TaskModel(context);

        return instance;
    }

    public ContentValues getContentValues(TaskData taskData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.COLUMN.TITLE, taskData.getTitle());
        contentValues.put(Task.COLUMN.DESCRIPTION, taskData.getDescription());
        contentValues.put(Task.COLUMN.CREATION_DATE, taskData.getCreationDate());
        contentValues.put(Task.COLUMN.PRIORITY_ID, taskData.getPriority().getId());
        contentValues.put(Task.COLUMN.DEADLINE_DATE, taskData.getDeadline());
        contentValues.put(Task.COLUMN.DONE, taskData.getDone() ? 1 : 0);

        return contentValues;
    }

    public long addTask(TaskData taskData) {
        long row = mDatabase.insert(Task.TABLE_NAME, null, getContentValues(taskData));
        setDeadlineInAlarmManager(row, taskData.getDeadline());

        return row;
    }

    public void updateTask(TaskData taskData) {
        boolean automaticDeleteCompleteTask = mSettingsApp.getBoolean("automatic_delete_complete_task", false);

        if (automaticDeleteCompleteTask && taskData.getDone()) {
            this.deleteTask(taskData.getId());

            return;
        }

        mDatabase.update(Task.TABLE_NAME, getContentValues(taskData), Task.COLUMN.ID + " = ?", new String[]{String.valueOf(taskData.getId())});
        setDeadlineInAlarmManager(taskData.getId(), taskData.getDeadline());
    }

    public void deleteTask(long taskId) {
        mDatabase.delete(Task.TABLE_NAME, Task.COLUMN.ID + " = ?", new String[] {String.valueOf(taskId)});
        setDeadlineInAlarmManager(taskId, 0);
    }

    public List<TaskData> getAllTask(boolean isTaskDone) {
        List<TaskData> taskDataList = new ArrayList<>();

        String whereSql = "";
        if (isTaskDone) {
            whereSql = " WHERE " + Task.COLUMN.DONE + " = 1";
        } else {
            whereSql = " WHERE " + Task.COLUMN.DONE + " = 0";
        }

        int sortingMethod = Integer.parseInt(mSettingsApp.getString("sort_task_list", "0"));
        boolean reverseSortingMethod = mSettingsApp.getBoolean("reverse_task_list", false);

        String sortSql = Task.COLUMN.CREATION_DATE;

        switch (sortingMethod) {
            case 0:
                sortSql = Task.COLUMN.ID;
            break;
            case 1:
                sortSql = Task.COLUMN.DEADLINE_DATE;
                break;
            case 2:
                sortSql = Task.COLUMN.PRIORITY_ID;
                break;
        }

        if (reverseSortingMethod) {
            sortSql += " ASC";
        } else {
            sortSql += " DESC";
        }

        String sqlQuery = "SELECT * FROM " + Task.TABLE_NAME + " t LEFT JOIN " + TaskPriority.TABLE_NAME + " tp ON t." + Task.COLUMN.PRIORITY_ID + " = tp." + TaskPriority.COLUMN.PRIORITY_ID + whereSql + " ORDER BY " + sortSql;
        Cursor cursor = mDatabase.rawQuery(sqlQuery, null);

        cursor.moveToFirst();
        while (! cursor.isAfterLast()) {
            TaskData taskData = getTaskObjectFromCursor(cursor);
            taskDataList.add(taskData);
            cursor.moveToNext();
        }
        cursor.close();

        return taskDataList;
    }

    public TaskData getTaskById(long taskId) {
        String sqlQuery = "SELECT * FROM " + Task.TABLE_NAME + " t LEFT JOIN " + TaskPriority.TABLE_NAME + " tp ON t." + Task.COLUMN.PRIORITY_ID + " = tp." + TaskPriority.COLUMN.PRIORITY_ID + " WHERE t." + Task.COLUMN.ID + " = " + taskId;
        Cursor cursor = mDatabase.rawQuery(sqlQuery, null);

        cursor.moveToFirst();
        TaskData taskData = getTaskObjectFromCursor(cursor);

        return taskData;
    }

    public void deleteListTask(List<TaskData> taskDataList) {
        StringBuilder sbTaskIds = new StringBuilder();

        for (TaskData taskData : taskDataList) {
            sbTaskIds.append(taskData.getId());
            sbTaskIds.append(",");
        }

        sbTaskIds.deleteCharAt(sbTaskIds.length() - 1);

        mDatabase.execSQL("DELETE FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN.ID + " IN (" + sbTaskIds.toString() + ")");
    }

    private TaskData getTaskObjectFromCursor(Cursor cursor) {
        // Собираем объект TaskPriority для объекта Task
        TaskData.Priority taskPriority = new TaskData.Priority();
        taskPriority.setId(cursor.getInt(cursor.getColumnIndex(Task.COLUMN.PRIORITY_ID)));
        taskPriority.setTitleRes(cursor.getInt(cursor.getColumnIndex(TaskPriority.COLUMN.TITLE_RES)));
        taskPriority.setColorRes(cursor.getInt(cursor.getColumnIndex(TaskPriority.COLUMN.COLOR_RES)));

        // Сам объект Task
        TaskData taskData = new TaskData();
        taskData.setId(cursor.getLong(cursor.getColumnIndex(Task.COLUMN.ID)));
        taskData.setTitle(cursor.getString(cursor.getColumnIndex(Task.COLUMN.TITLE)));
        taskData.setDescription(cursor.getString(cursor.getColumnIndex(Task.COLUMN.DESCRIPTION)));
        taskData.setCreationDate(cursor.getLong(cursor.getColumnIndex(Task.COLUMN.CREATION_DATE)));
        taskData.setPriority(taskPriority);
        taskData.setDeadline(cursor.getLong(cursor.getColumnIndex(Task.COLUMN.DEADLINE_DATE)));
        taskData.setDone(cursor.getInt(cursor.getColumnIndex(Task.COLUMN.DONE)) == 0 ? false : true);

        return taskData;
    }

    private void setDeadlineInAlarmManager(long taskId, long timeDeadline) {
        NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(mContextApp);

        if (timeDeadline > System.currentTimeMillis()) {
            notificationAlarmManager.setAlarm(timeDeadline, taskId);
        } else {
            notificationAlarmManager.cancelAlarm(taskId);
        }
    }
}