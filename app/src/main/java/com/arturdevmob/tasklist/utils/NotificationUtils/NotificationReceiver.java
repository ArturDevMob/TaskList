package com.arturdevmob.tasklist.utils.NotificationUtils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static Intent newIntent(Context context, long taskId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);

        return intent;
    }

    public static PendingIntent newPendingIntentBroadcast(Context context, long taskId) {
        Intent intent = NotificationReceiver.newIntent(context, taskId);

        return PendingIntent.getBroadcast(context, Long.valueOf(taskId).intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationTask notificationTask = new NotificationTask(context.getApplicationContext(), getTaskIdFromIntent(intent));
        notificationTask.show();
    }

    private long getTaskIdFromIntent(Intent intent) {
        Bundle args = intent.getExtras();

        if (args.containsKey(EXTRA_TASK_ID)) return args.getLong(EXTRA_TASK_ID);

        return -1;
    }
}
