package com.arturdevmob.tasklist.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.arturdevmob.tasklist.ui.fragment.TaskAddFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskAddActivity extends BaseActivityForFragment {
    public static final String EXTRA_DATA_TASK_ID = "task_id";

    @Override
    public Fragment createFragment() {
        TaskAddFragment fragment = new TaskAddFragment();
        return fragment;
    }

    public static Intent newIntent(Context parent) {
        return new Intent(parent, TaskAddActivity.class);
    }

    public static Intent newIntentWithData(Context parent, long taskId) {
        Intent intent = new Intent(parent, TaskAddActivity.class);
        intent.putExtra(EXTRA_DATA_TASK_ID, taskId);

        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
