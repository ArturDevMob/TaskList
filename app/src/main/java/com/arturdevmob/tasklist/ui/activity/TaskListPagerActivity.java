package com.arturdevmob.tasklist.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.ui.adapter.TaskListPagerAdapter;
import com.arturdevmob.tasklist.ui.fragment.TaskListFragment;
import com.arturdevmob.tasklist.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

public class TaskListPagerActivity extends BaseActivity {
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;
    private TaskListPagerAdapter mViewPagerAdapter;
    private List<TaskListFragment> mTaskListFragments;

    public static Intent newIntent(Context parent) {
        return new Intent(parent, TaskListPagerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_pager);

        mTaskListFragments = createListFragments();

        mViewPager = findViewById(R.id.view_pager);
        mPagerTabStrip = findViewById(R.id.pager_tab_strip);
        mViewPagerAdapter = new TaskListPagerAdapter(this, getSupportFragmentManager(), mTaskListFragments);
        mViewPager.setAdapter(mViewPagerAdapter);

        mPagerTabStrip.setTextColor(getResources().getColor(R.color.signal_black));
        mPagerTabStrip.setDrawFullUnderline(false);
        mPagerTabStrip.setTextSize(2, (float)16.0);
        mPagerTabStrip.setBackgroundResource(R.color.gainsborough);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.strawberry_red));
    }

    private List<TaskListFragment> createListFragments() {
        List<TaskListFragment> fragments = new ArrayList<>();

        fragments.add(TaskListFragment.newInstance(false));
        fragments.add(TaskListFragment.newInstance(true));

        return fragments;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.d("onActivityResult");

        for (TaskListFragment fragment : mTaskListFragments) {
            fragment.setDataFragment();
        }
    }
}