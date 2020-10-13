package com.example.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskManagerDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class TaskListFragment extends Fragment {
    public static final int REQUEST_CODE_EDIT = 0;
    public static final String TAG_EDIT_TASK = "EditTask";
    public static final String ARGS_INDEX = "Index";
    public static final String ARGS_USER_ID = "UserId";
    private RecyclerView mRecyclerView;
    private ImageView mImageViewEmpty;
    private TextView mTextSituation;
    private List<Task> mTasks = new ArrayList<>();
    private IRepositoryTask mRepositoryTask;
    private TaskAdapter mTaskAdapter;
    private int mIndex;
    private List<Task> mTasksDone = new ArrayList<>();
    private List<Task> mTasksDoing = new ArrayList<>();
    private List<Task> mTasksTodo = new ArrayList<>();
    private UUID mIdUser;
    private User mUser;
    private IRepositoryUser mRepositoryUser;
    private List<User> mUsers;
    private Task mTask;
    private State mStateBefore;

    public TaskListFragment() {
        // Required empty public constructor
    }


    public static TaskListFragment newInstance(int index, UUID uuid) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_INDEX, index);
        args.putSerializable(ARGS_USER_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryUser = TaskManagerDBRepository.getInstance(getActivity());
        mUsers = mRepositoryUser.getUserList();
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        mTasks = mRepositoryTask.getTaskList();


        //this is storage of this fragment
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARGS_INDEX);
            mIdUser = (UUID) getArguments().getSerializable(ARGS_USER_ID);
        }

        for (User user : mUsers) {
            if (user.getIDUser().equals(mIdUser))
                mUser = user;
        }
        selectAdminOrUser();
    }

    private void selectAdminOrUser() {
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
        for (Task task : mTasks) {
            if (task.getStateTask().equals(State.Todo) && task.getIdUser().equals(mIdUser))
                mTasksTodo.add(task);
            else if (task.getStateTask().equals(State.Doing) && task.getIdUser().equals(mIdUser))
                mTasksDoing.add(task);
            else if (task.getStateTask().equals(State.Done) && task.getIdUser().equals(mIdUser))
                mTasksDone.add(task);
        }
    }

    private void setTasksDoingDoneTodoAdmin() {
        for (Task task : mTasks) {
            if (task.getStateTask().equals(State.Todo))
                mTasksTodo.add(task);
            else if (task.getStateTask().equals(State.Doing))
                mTasksDoing.add(task);
            else if (task.getStateTask().equals(State.Done))
                mTasksDone.add(task);
        }
    }


    private void setViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_task_list);
        mImageViewEmpty = view.findViewById(R.id.image_empty_list);
        mTextSituation = view.findViewById(R.id.text_view_empty_task);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mTaskAdapter == null) {
            switch (mIndex) {
                case 0:
                    mTaskAdapter = new TaskAdapter(mTasksTodo);
                    break;
                case 1:
                    mTaskAdapter = new TaskAdapter(mTasksDoing);
                    break;
                case 2:
                    mTaskAdapter = new TaskAdapter(mTasksDone);
                    break;
            }
            mRecyclerView.setAdapter(mTaskAdapter);

        } else {
            switch (mIndex) {
                case 0:
                    mTaskAdapter.setTasksAdapter(mTasksTodo);
                    break;
                case 1:
                    mTaskAdapter.setTasksAdapter(mTasksDoing);
                    break;
                case 2:
                    mTaskAdapter.setTasksAdapter(mTasksDone);
                    break;
            }
            mTaskAdapter.notifyDataSetChanged();
        }

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
                    mStateBefore = mTaskHolder.getStateTask();
                    EditTaskFragment editTaskFragment =
                            EditTaskFragment.newInstance(mTaskHolder.getIdTask());
                    editTaskFragment.setTargetFragment(TaskListFragment.this,
                            REQUEST_CODE_EDIT);
                    editTaskFragment.show(getActivity().getSupportFragmentManager(), TAG_EDIT_TASK);

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
            mTitleTask.setText(task.getTitleTask());
            mDateTask.setText(task.getDateTask().toString());
            String title = String.valueOf(task.getTitleTask().charAt(0));
            mBtnTask.setText(title);
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
            Task task = mTasksAdapter.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount() {
            return mTasksAdapter.size();
        }
    }

    private void updateTask() {
        mRepositoryTask.updateTask(mTask);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;


        if (requestCode == REQUEST_CODE_EDIT) {
            UUID idTask;
            boolean check = data.getBooleanExtra(EditTaskFragment.EXTRA_SEND_CHECK_DELETE,
                    false);
            if(!check){
                idTask = (UUID) data.getSerializableExtra(EditTaskFragment.EXTRA_SEND_TASK_ID);
                for (Task taskFind : mTasks) {
                    if (taskFind.getIdTask().equals(idTask))
                        mTask = taskFind;

                }
                mTasks = mRepositoryTask.getTaskList();
                requestEditTask();

            } else {
                mTasks=mRepositoryTask.getTaskList();
                mTasksTodo.clear();
                mTasksDoing.clear();
                mTasksDone.clear();
                selectAdminOrUser();
                setAdapterTaskBefore();
                mTaskAdapter.notifyDataSetChanged();

            }


        }


    }

    private void requestEditTask() {
        mTasksTodo.clear();
        mTasksDoing.clear();
        mTasksDone.clear();
        selectAdminOrUser();
        setAdapterTaskBefore();
        switch (mTask.getStateTask()){
            case Todo:
                mTaskAdapter.setTasksAdapter(mTasksTodo);
                break;
            case Done:
                mTaskAdapter.setTasksAdapter(mTasksDone);
                break;
            case Doing:
                mTaskAdapter.setTasksAdapter(mTasksDoing);
                break;
        }
        mTaskAdapter.notifyDataSetChanged();
    }

    private void setAdapterTaskBefore() {
        switch (mStateBefore){
            case Todo:
                mTaskAdapter.setTasksAdapter(mTasksTodo);
                break;
            case Done:
                mTaskAdapter.setTasksAdapter(mTasksDone);
                break;
            case Doing:
                mTaskAdapter.setTasksAdapter(mTasksDoing);
                break;
        }
    }
}