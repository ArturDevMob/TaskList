package com.arturdevmob.tasklist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.database.DbScheme.Task;
import com.arturdevmob.tasklist.database.DbScheme.TaskPriority;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, DbScheme.DATABASE_NAME, null, DbScheme.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Таблица задач
        db.execSQL("CREATE TABLE " + Task.TABLE_NAME + " ("
                + Task.COLUMN.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Task.COLUMN.TITLE +" TEXT NOT NULL, "
                + Task.COLUMN.DESCRIPTION + " TEXT, "
                + Task.COLUMN.CREATION_DATE + " INTEGER NOT NULL, "
                + Task.COLUMN.PRIORITY_ID + " INTEGER NOT NULL DEFAULT 0, "
                + Task.COLUMN.DEADLINE_DATE + " INTEGER DEFAULT 0, "
                + Task.COLUMN.DONE + " INTEGER DEFAULT 0)"
        );

        // Таблица приоритетов для задач
        db.execSQL("CREATE TABLE " + TaskPriority.TABLE_NAME + " ("
                + TaskPriority.COLUMN.PRIORITY_ID + " INTEGER PRIMARY KEY, "
                + TaskPriority.COLUMN.TITLE_RES + " INTEGER, "
                + TaskPriority.COLUMN.COLOR_RES + " INTEGER)"
        );

        // Вставка приоритетов в таблицу Низкий, Средний, Высокий
        db.execSQL("INSERT INTO " + TaskPriority.TABLE_NAME + " (" + TaskPriority.COLUMN.PRIORITY_ID + ", " + TaskPriority.COLUMN.TITLE_RES + ", " + TaskPriority.COLUMN.COLOR_RES + ") values ('0','" + R.string.low + "','" + R.color.brilliant_green + "')");
        db.execSQL("INSERT INTO " + TaskPriority.TABLE_NAME + " (" + TaskPriority.COLUMN.PRIORITY_ID + ", " + TaskPriority.COLUMN.TITLE_RES + ", " + TaskPriority.COLUMN.COLOR_RES + ") values ('1','" + R.string.medium + "','" + R.color.signal_orange + "')");
        db.execSQL("INSERT INTO " + TaskPriority.TABLE_NAME + " (" + TaskPriority.COLUMN.PRIORITY_ID + ", " + TaskPriority.COLUMN.TITLE_RES + ", " + TaskPriority.COLUMN.COLOR_RES + ") values ('2','" + R.string.high + "','" + R.color.deep_yellow_pink + "')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
