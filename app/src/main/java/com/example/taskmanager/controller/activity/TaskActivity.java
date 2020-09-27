package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.fragment.CreateTaskFragment;
import com.example.taskmanager.controller.fragment.TaskListFragment;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TaskActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "com.example.taskmanager.UserId";
    public static final String TAG_CREATE_TASK = "createTask";
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private IRepositoryUser mRepositoryUser;
    private IRepositoryTask mRepositoryTask;
    private Button mButtonAdd;
    private UUID mIdUser;
    private List<Task> mTasks = new ArrayList<>();
    private List<User> mUsers;
    private List<State> mStates = Arrays.asList(State.values());
    private User mUser;
    private String userName;

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        mRepositoryTask = TaskRepository.getInstance();
        mRepositoryUser = UserRepository.getInstance();
        mTasks = mRepositoryTask.getTaskList();


        //this is storage of this Intent
        if (getIntent() != null) {
            mIdUser = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);
        }
        setViews();
        mUsers = mRepositoryUser.getUserList();
        initViews();
        setListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu,menu);
        MenuItem itemShow =menu.findItem(R.id.show_users);
        MenuItem itemDelete = menu.findItem(R.id.delete_all_task);
        if (userName.equals("admin")) {
            itemShow.setVisible(true);
            itemDelete.setVisible(false);
        } else {
            itemShow.setVisible(false);
            itemDelete.setVisible(true);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.show_users:
                return true;
            case R.id.delete_all_task:
                return true;
            case R.id.log_out:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setListener() {
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment =
                        CreateTaskFragment.newInstance(mRepositoryTask, mIdUser);
                createTaskFragment.show(getSupportFragmentManager(), TAG_CREATE_TASK);

            }
        });
    }

    private void setViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager_state_task);
        mButtonAdd = findViewById(R.id.btn_add);
    }

    private void initViews() {
        for (User userFind:mUsers) {
            if (userFind.getIDUser().equals(mIdUser))
                mUser = userFind;
        }
        userName = mUser.getUsername();
        AppCompatActivity activity = (AppCompatActivity) this;
        activity.getSupportActionBar().setTitle(mUser.getUsername());
        setListTask();
    }

    private void setListTask() {
        mViewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String nameTab = String.valueOf(mStates.get(position));
                tab.setText(nameTab);
            }
        }).attach();


    }


    public class ViewPagerAdapter extends FragmentStateAdapter {


        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return TaskListFragment.newInstance(position, mIdUser);
        }

        @Override
        public int getItemCount() {
            return mStates.size();
        }
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }


}