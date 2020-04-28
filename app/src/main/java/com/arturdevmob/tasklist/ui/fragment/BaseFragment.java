package com.arturdevmob.tasklist.ui.fragment;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void showMessage(int stringRes) {
        Snackbar.make(getView(), stringRes, Snackbar.LENGTH_LONG).show();
    }
}
