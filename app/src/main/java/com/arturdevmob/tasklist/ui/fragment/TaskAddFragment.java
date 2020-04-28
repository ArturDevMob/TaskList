package com.arturdevmob.tasklist.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.model.TaskModel;
import com.arturdevmob.tasklist.ui.activity.TaskAddActivity;
import com.arturdevmob.tasklist.utils.DateTimeDialog;
import com.arturdevmob.tasklist.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskAddFragment extends BaseFragment implements View.OnClickListener {
    private boolean mEditMode = false; // true - режим добавления; false - режим редактирования
    private TaskModel mTaskModel;
    private TaskData mTaskData;
    private TextView mSetDeadlineTextView;
    private EditText mTitleEditText, mDescriptionEditText;
    private Spinner mPrioritySpinner;
    private Button mAddButton, mCancelButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaskModel = TaskModel.getInstance(getContext().getApplicationContext());
        mTaskData = new TaskData();

        Bundle args = getActivity().getIntent().getExtras();
        if (args != null && args.containsKey(TaskAddActivity.EXTRA_DATA_TASK_ID)) {
            mTaskData = mTaskModel.getTaskById(args.getLong(TaskAddActivity.EXTRA_DATA_TASK_ID));
            mEditMode = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_add, container, false);

        initUI(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_deadline_text_view:
                setTimeDeadline();
            break;

            case R.id.add_button:
                setDataTaskObject();

                if (validationTaskObject(mTaskData)) {
                    if (mEditMode) {
                        mTaskModel.updateTask(mTaskData);
                    } else {
                        mTaskModel.addTask(mTaskData);
                    }

                    getActivity().setResult(getActivity().RESULT_OK);
                    getActivity().finish();
                } else {
                    showMessage(R.string.task_is_not_added);
                }

                break;

            case R.id.close_button:
                getActivity().setResult(getActivity().RESULT_CANCELED);
                getActivity().finish();
                break;
        }
    }

    private void initUI(View view) {
        mTitleEditText = view.findViewById(R.id.title_edit_text);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mPrioritySpinner = view.findViewById(R.id.priority_spinner);
        mSetDeadlineTextView = view.findViewById(R.id.set_deadline_text_view);
        mAddButton = view.findViewById(R.id.add_button);
        mCancelButton = view.findViewById(R.id.close_button);

        mPrioritySpinner.setAdapter(createSpinnerAdapter());
        mSetDeadlineTextView.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        if (mEditMode) {
            mTitleEditText.setText(mTaskData.getTitle());
            mDescriptionEditText.setText(mTaskData.getDescription());
            mPrioritySpinner.setSelection(mTaskData.getPriority().getId());

            if (mTaskData.getDeadline() > 0) {
                mSetDeadlineTextView.setText(Utils.getFormattedDateString(mTaskData.getDeadline()));
            }
        }
    }

    private void setDataTaskObject() {
        TaskData.Priority taskPriority = new TaskData.Priority();
            taskPriority.setId(mPrioritySpinner.getSelectedItemPosition());

        if (mTaskData == null) {
            mTaskData = new TaskData();
        }

        mTaskData.setTitle(mTitleEditText.getText().toString());
        mTaskData.setDescription(mDescriptionEditText.getText().toString());
        mTaskData.setCreationDate(new Date().getTime());
        mTaskData.setPriority(taskPriority);
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        String[] arrPriority = getResources().getStringArray(R.array.priorities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrPriority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private void setTimeDeadline() {
        DateTimeDialog.OnEventDate callback = new DateTimeDialog.OnEventDate() {
            @Override
            public void onSetDate(long date) {
                String snackBarMessage;
                mTaskData.setDeadline(date);
                if (date > System.currentTimeMillis()) {
                    mSetDeadlineTextView.setText(Utils.getFormattedDateString(date));
                    snackBarMessage = getString(R.string.deadline_installed);
                } else {
                    mSetDeadlineTextView.setText(R.string.set_date_deadline);
                    snackBarMessage = getString(R.string.deadline_not_installed);
                }

                Snackbar.make(getView(), snackBarMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onRemoveDate() {
                mTaskData.setDeadline(0);
                mSetDeadlineTextView.setText(R.string.set_date_deadline);
                Snackbar.make(getView(), getString(R.string.deadline_remove), Snackbar.LENGTH_LONG).show();
            }
        };

        DateTimeDialog dateTimeDialog = new DateTimeDialog(getContext(), callback);
    }

    private boolean validationTaskObject(TaskData taskData) {
        if (taskData.getTitle().length() > 1)
            return true;

        return false;
    }
}