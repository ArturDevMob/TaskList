package com.arturdevmob.tasklist.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.model.TaskModel;
import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.ui.activity.TaskActivity;
import com.arturdevmob.tasklist.ui.adapter.TaskListAdapter;
import com.arturdevmob.tasklist.ui.activity.TaskAddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListFragment extends BaseFragment {
    public static final String ARGS_IS_TASK_DONE = "is_task_done";
    public static final int REQUEST_CODE_ADD = 0;
    public static final int REQUEST_CODE_TASK_ACTIVITY = 3;

    private TaskModel mTaskModel;
    public List<TaskData> mTaskDataList;
    private RecyclerView mRecyclerView;
    private TextView mEmptyListTextView;
    private TaskListAdapter mTaskListAdapter;
    private FloatingActionButton mFab;

    public static TaskListFragment newInstance(boolean isTaskDone) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_IS_TASK_DONE, isTaskDone);

        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskModel = TaskModel.getInstance(getContext().getApplicationContext());
        mTaskDataList = mTaskModel.getAllTask(getArgsTask());
        mTaskListAdapter = new TaskListAdapter(getContext(), mTaskDataList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        mEmptyListTextView = view.findViewById(R.id.empty_list);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.hasFixedSize();
        mRecyclerView.setAdapter(mTaskListAdapter);

        showPlugIfTaskListEmpty();

        mFab = view.findViewById(R.id.fab);

        // Для фрагмента с активными задачами, открываем активити с добавлением новой задачи
        // Для фрагмента с АРХИВНЫМИ задачами, удаляем весь список
        if (getArgsTask()) {
            mEmptyListTextView.setText(R.string.archive_task_list_is_empty);
            mFab.setImageResource(R.drawable.basket);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cleanArchiveTask();
                }
            });
        } else {
            mEmptyListTextView.setText(R.string.task_list_is_empty);
            mFab.setImageResource(R.drawable.add_item_list);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runTaskAddActivity();
                }
            });
        }

        mTaskListAdapter.callback = new TaskListAdapter.OnFragmentCallback() {
            @Override
            public void startTaskActivity(long taskId) {
                Intent intent = TaskActivity.newIntent(getActivity(), taskId);
                startActivityForResult(intent, REQUEST_CODE_TASK_ACTIVITY);
            }
        };

        return view;
    }

    private void runTaskAddActivity() {
        startActivityForResult(TaskAddActivity.newIntent(getActivity()), REQUEST_CODE_ADD);
    }

    public void setDataFragment() {
        mTaskDataList.clear();
        mTaskDataList.addAll(mTaskModel.getAllTask(getArgsTask()));
        mTaskListAdapter.notifyDataSetChanged();

        showPlugIfTaskListEmpty();
    }

    private boolean getArgsTask() {
        return getArguments().getBoolean(ARGS_IS_TASK_DONE);
    }

    private void showPlugIfTaskListEmpty() {
        // Показать заглушку если список задач пустой
        if (! mTaskDataList.isEmpty()) {
            mEmptyListTextView.setVisibility(View.GONE);
        } else {
            mEmptyListTextView.setVisibility(View.VISIBLE);
            mEmptyListTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runTaskAddActivity();
                }
            });
        }
    }

    private void cleanArchiveTask() {
        String messageSnackBar = "";

        if (! mTaskDataList.isEmpty()) {
            mTaskModel.deleteListTask(mTaskDataList);
            setDataFragment();

            messageSnackBar = getResources().getString(R.string.list_task_complete_deleted);
        } else {
            messageSnackBar = getResources().getString(R.string.list_task_empty);
        }

        Snackbar.make(this.getView(), messageSnackBar, Snackbar.LENGTH_LONG).show();
    }
}