package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity {
    public static final String EXTRA_USER_REPOSITORY = "com.example.taskmanager.UserRepository";
    public static final String EXTRA_TASK_REPOSITORY = "com.example.taskmanager.TaskRepository";
    public static final String EXTRA_USER_ID = "com.example.taskmanager.UserId";
    public static final String TAG_CREATE_TASK = "createTask";
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private TextView mTextUserName;
    private IRepositoryUser mRepositoryUser;
    private IRepositoryTask mRepositoryTask;
    private Button mButtonAdd;
    private Button mButtonLogout;
    private Button mButtonDeleteAllTasks;
    private UUID mIdUser;
    private State mStateTask;
    List<User> mUsers;

    public static Intent newIntent(Context context, IRepositoryTask repositoryTask, UUID id) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_TASK_REPOSITORY, repositoryTask);
        intent.putExtra(EXTRA_USER_ID, id);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        mRepositoryTask = TaskRepository.getInstance();
        mRepositoryUser = UserRepository.getInstance();

        //get intent
        mRepositoryUser = (IRepositoryUser) getIntent().getSerializableExtra(EXTRA_USER_REPOSITORY);
        mRepositoryTask = (IRepositoryTask) getIntent().getSerializableExtra(EXTRA_TASK_REPOSITORY);
        mIdUser = (UUID) getIntent().getSerializableExtra(EXTRA_USER_ID);

        setViews();
        mUsers = mRepositoryUser.getUserList();
        for (User user: mUsers) {
            if (user.getIDUser().equals(mIdUser))
                mTextUserName.setText(user.getUsername());
        }
        initViews();
        setListener();
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
        mTextUserName = findViewById(R.id.text_user_name);
        mButtonAdd = findViewById(R.id.btn_add);
        mButtonLogout = findViewById(R.id.btn_logout);
        mButtonDeleteAllTasks = findViewById(R.id.btn_delete_all_tasks);
    }

    private void initViews() {
        setListTask();
    }

    private void setListTask() {
        List<State> states = Arrays.asList(State.values());
        final List<State> finalStates = states;
        StateTaskPagerAdapter adapter = new
                StateTaskPagerAdapter(this, states);
        mViewPager.setAdapter(adapter);
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String nameState = String.valueOf(finalStates.get(position));
                tab.setText(nameState + (position + 1));
            }
        }).attach();
    }

    private class StateTaskPagerAdapter extends FragmentStateAdapter {
        List<State> mStates;

        public StateTaskPagerAdapter(@NonNull FragmentActivity fragmentActivity,
                                     List<State> states) {
            super(fragmentActivity);
            mStates = states;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            State state = mStates.get(position);
            TaskListFragment stateFragment = TaskListFragment.newInstance
                    (state, mRepositoryTask, mIdUser);
            return stateFragment;
        }

        @Override
        public int getItemCount() {
            return mStates.size();
        }
    }


}