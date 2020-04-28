package com.arturdevmob.tasklist.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.model.TaskModel;
import com.arturdevmob.tasklist.utils.NotificationUtils.NotificationAlarmManager;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    public static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context.getApplicationContext();

        if (intent.getAction().equals(ACTION)) {
            setAlarmTaskList();
        }
    }

    private void setAlarmTaskList() {
        List<TaskData> listTaskData = TaskModel.getInstance(mContext).getAllTask(false);

        for (TaskData taskData : listTaskData) {
            if (taskData.getDeadline() > 0) {
                NotificationAlarmManager notificationAlarmManager = new NotificationAlarmManager(mContext);
                notificationAlarmManager.setAlarm(taskData.getDeadline(), taskData.getId());
            }
        }
    }
}
