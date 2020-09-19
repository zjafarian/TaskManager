package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.controller.fragment.TaskListFragment;
import com.example.taskmanager.model.State;
import com.example.taskmanager.repository.IRepositoryTask;

import java.util.UUID;

public class TaskListActivity extends SingleFragmentActivity {

    public static final String EXTRA_TASK_REPOSITORY = "com.example.taskmanager.TaskRepository";
    public static final String EXTRA_USER_ID = "com.example.taskmanager.UserId";
    public static final String EXTRA_STATE_TASK = "com.example.taskmanager.stateTask";


    public static Intent newIntent(Context context, State state, IRepositoryTask repositoryTask, UUID id) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_TASK_REPOSITORY, repositoryTask);
        intent.putExtra(EXTRA_USER_ID,id);
        intent.putExtra(EXTRA_STATE_TASK,state);

        return intent;
    }


    @Override
    public Fragment createFragment() {
        IRepositoryTask repositoryTask = (IRepositoryTask) getIntent().getSerializableExtra(EXTRA_TASK_REPOSITORY);
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        State state = (State) getIntent().getSerializableExtra(EXTRA_STATE_TASK);
        TaskListFragment taskListFragment = TaskListFragment.newInstance(state,repositoryTask,id);
        return taskListFragment;
    }
}