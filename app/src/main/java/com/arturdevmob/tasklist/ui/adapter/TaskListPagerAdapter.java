package com.arturdevmob.tasklist.ui.adapter;

import android.content.Context;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.ui.fragment.TaskListFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TaskListPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<TaskListFragment> mFragmentsList;

    public TaskListPagerAdapter(Context context, FragmentManager fm, List<TaskListFragment> fragmentsList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mFragmentsList = fragmentsList;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.active_task);
            case 1:
                return mContext.getString(R.string.archival_task);
        }

        return null;
    }
}
