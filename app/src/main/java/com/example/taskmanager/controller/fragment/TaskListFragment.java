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
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String ARGS_ID_USER = "ArgsIdUser";
    public static final String ARGS_INDEX = "argsIndex";
    private RecyclerView mRecyclerView;
    private IRepositoryTask mRepositoryTask;
    private int mIndex;
    private List<Task> mTasks = new ArrayList<>();
    private UUID mIdUser;
    private List<Task> mTasksDone = new ArrayList<>();
    private List<Task> mTasksDoing = new ArrayList<>();
    private List<Task> mTasksTodo = new ArrayList<>();
    private List<User> mUserList = new ArrayList<>();
    private IRepositoryUser mRepositoryUser;
    private User mUser;

    public TaskListFragment() {
        // Required empty public constructor
    }


    public static TaskListFragment newInstance(int index, UUID id) {
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
        mRepositoryUser = UserRepository.getInstance();
        mUserList = mRepositoryUser.getUserList();

        //this is storage of this fragment
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARGS_INDEX);
            mIdUser = (UUID) getArguments().getSerializable(ARGS_ID_USER);
        }
        for (User user : mUserList) {
            if (user.getIDUser().equals(mIdUser))
                mUser = user;
        }
        if (mUser.getUsername().equals("admin") && mUser.getPassword().equals("admin"))
            setTasksDoingDoneTodoAdmin();
        else
            setTasksDoingDoneTodoUser();
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


    private void setTasksDoingDoneTodoUser() {
        switch (mIndex) {
            case 0:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Todo) && task.getIdUser().equals(mIdUser))
                        mTasksTodo.add(task);
                }
                break;
            case 1:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Doing) && task.getIdUser().equals(mIdUser))
                        mTasksDoing.add(task);
                }
                break;
            case 2:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Done) && task.getIdUser().equals(mIdUser))
                        mTasksDone.add(task);
                }
                break;
        }
    }

    private void setTasksDoingDoneTodoAdmin() {
        switch (mIndex) {
            case 0:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Todo))
                        mTasksTodo.add(task);
                }
                break;
            case 1:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Doing))
                        mTasksDoing.add(task);
                }
                break;
            case 2:
                for (Task task : mTasks) {
                    if (task.getStateTask().equals(State.Done))
                        mTasksDone.add(task);
                }
                break;
        }
    }

    private void setViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_task_list);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        switch (mIndex) {
            case 0:
                setAdapterTaskList(mTasksTodo);
                break;
            case 1:
                setAdapterTaskList(mTasksDoing);
                break;
            case 2:
                setAdapterTaskList(mTasksDone);
                break;
        }

    }

    private void setAdapterTaskList(List<Task> tasks) {
        mTasks = mRepositoryTask.getTaskList();
        TaskAdapter taskAdapter = new TaskAdapter(tasks);
        mRecyclerView.setAdapter(taskAdapter);
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTask;
        private TextView mDateTask;
        private Button mBtnTask;
        private Task mTaskHolder;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            setViews(itemView);
            setListener(itemView);
        }

        private void setListener(@NonNull View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(mTaskHolder);

                }
            });
        }

        private void setViews(@NonNull View itemView) {
            mTitleTask = itemView.findViewById(R.id.text_title_task);
            mDateTask = itemView.findViewById(R.id.text_date_task);
            mBtnTask = itemView.findViewById(R.id.btn_first_title_task);
        }

        public void bindTask(Task task) {
            mTaskHolder = task;
            if (mTaskHolder.getIdUser().equals(mIdUser)) {
                mTitleTask.setText(task.getTitleTask());
                mDateTask.setText(task.getDateTask().toString());
                String title = task.getTitleTask();
                mBtnTask.setText(title.charAt(0));
            }
        }


    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskListFragment.TaskHolder> {
        private List<Task> mTasksAdapter;

        public TaskAdapter(List<Task> taskList) {
            mTasksAdapter = taskList;
        }

        public List<Task> getTasksAdapter() {
            return mTasksAdapter;
        }

        public void setTasksAdapter(List<Task> tasksAdapter) {
            mTasksAdapter = tasksAdapter;
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
            Task task =null;
            if (mIndex==position){
                task = mTasksAdapter.get(position);
            }
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasksAdapter.size();
        }
    }


}