package com.arturdevmob.tasklist.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.arturdevmob.tasklist.ui.fragment.SettingsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsActivity extends BaseActivityForFragment {
    public static Intent newIntent(Context parent) {
        return new Intent(parent, SettingsActivity.class);
    }

    @Override
    public Fragment createFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();

        return fragment;
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
