package com.example.taskmanager.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;

import java.util.List;


public class TaskPerStateFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private IRepositoryTask mRepositoryTask;
    private User mUser;
    private List<Task> mTasks;


    public TaskPerStateFragment() {
        // Required empty public constructor
    }


    public static TaskPerStateFragment newInstance() {
        TaskPerStateFragment fragment = new TaskPerStateFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskRepository.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_per_state, container, false);
        setViews(view);
        initViews();
        return view;
    }

    private void setViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_task_list);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTasks = mRepositoryTask.getTaskList();
        TaskAdapter taskAdapter = new TaskAdapter(mTasks);
        mRecyclerView.setAdapter(taskAdapter);
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTask;
        private TextView mDateTask;
        private Button mBtnTask;
        private Task mTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            setViews(itemView);
        }

        private void setViews(@NonNull View itemView) {
            mTitleTask = itemView.findViewById(R.id.text_title_task);
            mDateTask = itemView.findViewById(R.id.text_date_task);
            mBtnTask = itemView.findViewById(R.id.btn_first_title_task);
        }

        public void bindTask(Task task) {
            mTask = task;
            mTitleTask.setText(task.getTitleTask());
            mDateTask.setText(task.getDateTask().toString());
            String title = task.getTitleTask();
            mBtnTask.setText(title.charAt(0));
        }


    }


    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> mTasks;

        public TaskAdapter(List<Task> taskList) {
            mTasks = taskList;
        }

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }


        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list, parent, false);
            TaskHolder taskHolder =new TaskHolder(view);
            return taskHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }



}