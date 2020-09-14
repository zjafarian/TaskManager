package com.example.taskmanager.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.fragment.TaskPerStateFragment;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private TextView mTextUserName;
    private IRepositoryUser mRepositoryUser;
    private IRepositoryTask mRepositoryTask;
    private int currentIndex;
    private Button mButtonAdd;
    private Button mButtonLogout;
    private Button mButtonDeleteAllTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mRepositoryTask = TaskRepository.getInstance();
        mRepositoryUser = UserRepository.getInstance();
        setViews();
        initViews();
        setListener();

    }
    private void setListener() {
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager_state_task);
        mTextUserName = findViewById(R.id.text_user_name);
        mButtonAdd = findViewById(R.id.btn_add);
        mButtonLogout =findViewById(R.id.btn_logout);
        mButtonDeleteAllTasks = findViewById(R.id.btn_delete_all_tasks);
    }

    private void initViews() {

        List<State> states = new ArrayList<>();
        states = Arrays.asList(State.values());
        final List<State> finalStates = states;
        StateTaskPagerAdapter adapter = new StateTaskPagerAdapter(this, states);
        mViewPager.setAdapter(adapter);
        new TabLayoutMediator(mTabLayout,mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String nameState = String.valueOf(finalStates.get(position));
                tab.setText(nameState + (position +1));

            }
        }).attach();



    }

    private class StateTaskPagerAdapter extends FragmentStateAdapter{
        List<State> mStates;


        public StateTaskPagerAdapter(@NonNull FragmentActivity fragmentActivity,List<State> states) {
            super(fragmentActivity);
            mStates = states;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            State state = mStates.get(position);
            TaskPerStateFragment stateFragment = TaskPerStateFragment.newInstance(state,mRepositoryTask);
            return stateFragment;
        }

        @Override
        public int getItemCount() {
            return mStates.size();
        }
    }
}