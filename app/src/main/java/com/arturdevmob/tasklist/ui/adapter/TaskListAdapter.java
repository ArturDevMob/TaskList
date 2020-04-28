package com.arturdevmob.tasklist.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arturdevmob.tasklist.R;
import com.arturdevmob.tasklist.model.TaskData;
import com.arturdevmob.tasklist.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private Context mContext;
    private List<TaskData> mTaskDataList;
    public OnFragmentCallback callback;

    public interface OnFragmentCallback {
        void startTaskActivity(long taskId);
    }

    public TaskListAdapter(Context mContext, List<TaskData> mTaskDataList) {
        this.mContext = mContext;
        this.mTaskDataList = mTaskDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_task_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mTaskDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTaskDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TaskData mTaskData;
        private View mPriorityView;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mDeadlineDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();

            mPriorityView = itemView.findViewById(R.id.priority_view);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mDescriptionTextView = itemView.findViewById(R.id.description_text_view);
            mDeadlineDateTextView = itemView.findViewById(R.id.deadline_date_text_view);
        }

        public void bind(TaskData taskData) {
            mTaskData = taskData;

            mPriorityView.setBackgroundResource(mTaskData.getPriority().getColorRes());
            mTitleTextView.setText(mTaskData.getTitle());
            mDescriptionTextView.setText(Utils.trimString(mTaskData.getDescription()));

            // Если дедлайн установлен, выводим дату, иначе текст
            if (mTaskData.getDeadline() > 0) {
                mDeadlineDateTextView.setText(Utils.getFormattedDateString(mTaskData.getDeadline()));
            } else {
                mDeadlineDateTextView.setText(R.string.no_time_limit);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.startTaskActivity(mTaskData.getId());
                }
            });
        }
    }
}