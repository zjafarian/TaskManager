package com.example.taskmanager.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String ARGS_STATE = "argsState";
    public static final String ARGS_TASK_REPOSITORY = "ArgsTaskRepository";
    public static final String ARGS_ID_USER = "ArgsIdUser";
    private RecyclerView mRecyclerView;
    private IRepositoryTask mRepositoryTask;
    private User mUser;
    private List<Task> mTasks;
    private State mState;
    private UUID mIdUser;


    public TaskListFragment() {
        // Required empty public constructor
    }


    public static TaskListFragment newInstance(State state, IRepositoryTask repositoryTask,
                                               UUID id) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_STATE, state);
        args.putSerializable(ARGS_TASK_REPOSITORY, repositoryTask);
        args.putSerializable(ARGS_ID_USER, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskRepository.getInstance();

        //this is storage of this fragment
        if (getArguments() != null) {
            mRepositoryTask = (IRepositoryTask) getArguments().getSerializable(ARGS_TASK_REPOSITORY);
            mIdUser = (UUID) getArguments().getSerializable(ARGS_ID_USER);
            mState = (State) getArguments().getSerializable(ARGS_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
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
        if (mTasks.size() == 0) {

        } else {
            TaskListFragment.TaskAdapter taskAdapter = new TaskListFragment.TaskAdapter(mTasks);
            mRecyclerView.setAdapter(taskAdapter);
        }

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
            if (mTask.getStateTask().equals(mState)) {
                mTitleTask.setText(task.getTitleTask());
                mDateTask.setText(task.getDateTask().toString());
                String title = task.getTitleTask();
                mBtnTask.setText(title.charAt(0));
            }
        }


    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskListFragment.TaskHolder> {
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
        public TaskListFragment.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_row_list, parent, false);
            TaskListFragment.TaskHolder taskHolder = new TaskListFragment.TaskHolder(view);
            return taskHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskListFragment.TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }


}