package com.arturdevmob.tasklist.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.ui.fragment.AboutAppDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SETTINGS = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.app_link)));

        switch (id) {
            case R.id.settings:
                startActivitySettings();
            break;

            /*case R.id.leave_review:
                startActivity(intent);
            break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    public void startActivitySettings() {
        Intent intent = SettingsActivity.newIntent(this);

        startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    private void showAboutAppDialog() {
        AboutAppDialog dialog = new AboutAppDialog();
        dialog.show(getSupportFragmentManager(), "ABOUT_APP");
    }
}
