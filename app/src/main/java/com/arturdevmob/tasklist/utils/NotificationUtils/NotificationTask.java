package com.arturdevmob.tasklist.utils.NotificationUtils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.model.TaskModel;
import com.arturdevmob.tasklist.ui.activity.TaskActivity;

import androidx.core.app.NotificationCompat;

public class NotificationTask {
    public static final String CHANEL_ID = "NOTIFICATION_DEADLINE_TASK";

    private Context mContext;
    private TaskData mTaskData;

    public NotificationTask(Context context, long taskId) {
        mContext = context;
        mTaskData = getTaskData(taskId);
    }

    public void show() {
        PendingIntent pendingIntent = TaskActivity.newPendingIntentActivity(mContext, mTaskData.getId());

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_notification_task_icon)
                .setContentTitle(mTaskData.getTitle())
                .setContentText(String.format("%s %s", mContext.getString(R.string.burning_deadline), mContext.getString(R.string.click_to_go_to_ask)))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, mContext.getString(R.string.chanel_task_description), NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);

            manager.createNotificationChannel(channel);
        }

        manager.notify(Long.valueOf(mTaskData.getId()).intValue(), notification.build());
    }

    private TaskData getTaskData(long taskId) {
        return TaskModel.getInstance(mContext).getTaskById(taskId);
    }
}
