package com.arturdevmob.tasklist.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.arturdevmob.tasklist.BuildConfig;
import com.arturdevmob.tasklist.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AboutAppDialog extends DialogFragment {
    private TextView mVersionApp;
    private Button mCloseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app, null, false);

        initUI(view);

        setDataUI();

        return view;
    }

    private void initUI(View view) {
        mVersionApp = view.findViewById(R.id.version_app);
        mCloseButton = view.findViewById(R.id.close_button);
    }

    private void setDataUI() {
        mVersionApp.setText(String.format("%s %s", getString(R.string.version_app), BuildConfig.VERSION_NAME));
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
