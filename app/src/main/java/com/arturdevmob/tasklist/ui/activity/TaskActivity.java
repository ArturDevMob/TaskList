package com.arturdevmob.tasklist.ui.activity;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.arturdevmob.tasklist.ui.fragment.TaskFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskActivity extends BaseActivityForFragment {
    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    public Fragment createFragment() {
        long taskId = getIntent().getLongExtra(EXTRA_TASK_ID, 0);
        TaskFragment fragment = TaskFragment.newInstance(taskId);

        return fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Intent newIntent(Context parent, long taskId) {
        Intent intent = new Intent(parent, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);

        return intent;
    }

    public static PendingIntent newPendingIntentActivity(Context context, long taskId) {
        Intent intent = TaskActivity.newIntent(context, taskId);

        return PendingIntent.getActivity(context, Long.valueOf(taskId).intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = TaskListPagerActivity.newIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
