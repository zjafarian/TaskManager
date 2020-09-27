package com.example.taskmanager.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String ARGS_ID_USER = "ArgsIdUser";
    public static final String ARGS_INDEX = "argsIndex";
    private RecyclerView mRecyclerView;
    private IRepositoryTask mRepositoryTask;
    private ImageView mImageViewEmpty;
    private int mIndex;
    private List<Task> mTasks = new ArrayList<>();
    private UUID mIdUser;


    public TaskListFragment() {
        // Required empty public constructor
    }


    public static TaskListFragment newInstance(int index,
                                               UUID id) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_INDEX, index);
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
            mIndex = getArguments().getInt(ARGS_INDEX);
            mIdUser = (UUID) getArguments().getSerializable(ARGS_ID_USER);
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
        mImageViewEmpty = view.findViewById(R.id.image_empty);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mTasks.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        } else {
            mImageViewEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mTasks = mRepositoryTask.getTaskList();
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
            setListener(itemView);
        }

        private void setListener(@NonNull View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mTask);

                }
            });
        }

        private void setViews(@NonNull View itemView) {
            mTitleTask = itemView.findViewById(R.id.text_title_task);
            mDateTask = itemView.findViewById(R.id.text_date_task);
            mBtnTask = itemView.findViewById(R.id.btn_first_title_task);
        }

        public void bindTask(Task task) {
            mTask = task;
            if (mTask.getIdUser().equals(mIdUser)) {
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
            Task task = null;
            if (mIndex == position) {
                task = mTasks.get(position);
            }
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }


}