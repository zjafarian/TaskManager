package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.controller.fragment.TaskListFragment;

import java.util.UUID;

public class TaskListActivity extends SingleFragmentActivity {

    public static final String EXTRA_INDEX = "com.example.taskmanager.index";
    public static final String EXTRA_USER_ID = "com.example.taskmanager.UserId";


    public static Intent newIntent(Context context, int index, UUID id) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_INDEX,index);
        intent.putExtra(EXTRA_USER_ID,id);
        return intent;
    }


    @Override
    public Fragment createFragment() {
        int index = getIntent().getIntExtra(EXTRA_INDEX,0);
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        TaskListFragment taskListFragment = TaskListFragment.newInstance(index,id);
        return taskListFragment;
    }
}