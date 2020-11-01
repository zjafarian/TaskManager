package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.fragment.TaskFragment;
import com.example.taskmanager.controller.fragment.TaskListFragment;

import java.util.UUID;

public class TaskActivity extends SingleFragmentActivity implements TaskListFragment.CallBacks {

    public static final String EXTRA_USER_ID = "com.example.taskmanager.UserId";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        TaskFragment taskFragment=TaskFragment.newInstance(uuid);
        return taskFragment;
    }

    @Override
    public void updateAdapterPager() {
       TaskFragment taskFragment = (TaskFragment)
               getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        taskFragment.setListTask();


    }
}
