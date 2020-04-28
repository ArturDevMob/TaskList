package com.arturdevmob.tasklist.utils.NotificationUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class NotificationAlarmManager {
    private Context mContext;
    private AlarmManager mAlarmManager;

    public NotificationAlarmManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(long time, long taskId) {
        PendingIntent pendingIntent = NotificationReceiver.newPendingIntentBroadcast(mContext.getApplicationContext(), taskId);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public void cancelAlarm(long taskId) {
        PendingIntent pendingIntent = NotificationReceiver.newPendingIntentBroadcast(mContext.getApplicationContext(), taskId);

        mAlarmManager.cancel(pendingIntent);
    }
}