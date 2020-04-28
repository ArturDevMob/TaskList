package com.arturdevmob.tasklist.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.model.TaskModel;
import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.ui.activity.TaskAddActivity;
import com.arturdevmob.tasklist.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskFragment extends Fragment {
    public static final String ARGS_TASK_ID = "task_id";
    public static final int REQUEST_CODE_EDIT = 0;

    private TaskModel mTaskModel;
    private TaskData mTaskData;
    private View mPriorityView;
    private TextView mTitleTextView, mDescriptionTextView, mDeadlineDateTextView;
    private Button mActionTaskButton;

    public static TaskFragment newInstance(long taskId) {
        Bundle args = new Bundle();
        args.putLong(ARGS_TASK_ID, taskId);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskModel = TaskModel.getInstance(getContext().getApplicationContext());
        mTaskData = mTaskModel.getTaskById(getIdTaskFromArgsFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        initUI(view);

        setDataUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && resultCode == Activity.RESULT_OK) {
            mTaskData = mTaskModel.getTaskById(getIdTaskFromArgsFragment());
            setDataUI();
        }
    }

    private void initUI(View view) {
        mPriorityView = view.findViewById(R.id.priority_view);
        mTitleTextView = view.findViewById(R.id.title_text_view);
        mDescriptionTextView = view.findViewById(R.id.description_text_view);
        mDeadlineDateTextView = view.findViewById(R.id.deadline_date_text_view);
        mActionTaskButton = view.findViewById(R.id.action_task_button);
    }

    private void setDataUI() {
        mPriorityView.setBackgroundResource(mTaskData.getPriority().getColorRes());
        mTitleTextView.setText(mTaskData.getTitle());
        mDescriptionTextView.setText(mTaskData.getDescription());
        mActionTaskButton.setOnClickListener(onClickListenerActionButton);

        // Если дедлайн установлен, выводим дату, иначе текст
        if (mTaskData.getDeadline() > 0) {
            mDeadlineDateTextView.setText(Utils.getFormattedDateString(mTaskData.getDeadline()));
        } else {
            mDeadlineDateTextView.setText(R.string.no_time_limit);
        }
    }

    private void changeStatusTask() {
        if (mTaskData.getDone()) {
            mTaskData.setDone(false);
        } else {
            mTaskData.setDone(true);
        }

        mTaskModel.updateTask(mTaskData);
    }

    private void setResultAndFinishActivity(int resultCode) {
        getActivity().setResult(resultCode);
        getActivity().finish();
    }

    private void startTaskAddActivityForResult() {
        Intent intent = TaskAddActivity.newIntentWithData(getActivity(), mTaskData.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    private long getIdTaskFromArgsFragment() {
        return getArguments().getLong(ARGS_TASK_ID);
    }

    private AdapterView.OnClickListener onClickListenerActionButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogActionTask();
        }
    };

    public void showDialogActionTask() {
        View view = View.inflate(getContext(), R.layout.fragment_action_task, null);
        final AlertDialog dialog;
        ListView actionTaskListView = view.findViewById(R.id.action_task_list_view);

        final String[] arrayString = getResources().getStringArray(R.array.action_task);

        // Если задача выполнена, меняем строку в первом элементе с "Выполнена" на "Восстановить"
        if (mTaskData.getDone()) {
            arrayString[0] = getString(R.string.recover);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, arrayString);
        actionTaskListView.setAdapter(adapter);

        dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.close, null)
                .create();

        actionTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Выбран элемент "Выполнена" или "Восстановить"
                        changeStatusTask();
                        setResultAndFinishActivity(Activity.RESULT_OK);
                    break;
                    case 1:
                        // Выбран элемент "Редактирова"
                        startTaskAddActivityForResult();
                    break;
                    case 2:
                        // Выбран элемент "Удалить"
                        mTaskModel.deleteTask(mTaskData.getId());
                        setResultAndFinishActivity(Activity.RESULT_OK);
                    break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}