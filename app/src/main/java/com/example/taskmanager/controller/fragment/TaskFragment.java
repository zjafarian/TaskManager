package com.example.taskmanager.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.activity.ListAllUsersActivity;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskManagerDBRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class TaskFragment extends Fragment {

    public static final String ARGS_USER_ID = "UserId";
    public static final String TAG_CREATE_TASK = "CreateTask";
    public static final int REQUEST_CODE_CREATE_TASK = 0;
    public static final int REQUEST_CODE_DELETE_ALL_TASKS = 1;
    public static final String TAG_DELETE_ALL_TASKS = "deleteAllTasks";
    public static final int REQUEST_CODE_SHOW_USERS = 2;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private IRepositoryUser mRepositoryUser;
    private IRepositoryTask mRepositoryTask;
    private UUID mIdUser;
    private List<Task> mTasks = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();
    private List<State> mStates = Arrays.asList(State.values());
    private User mUser;
    private String userName;
    private ViewPagerAdapter mAdapter;
    private FloatingActionButton mButtonAdd;

    public TaskFragment() {
        // Required empty public constructor
    }


    public static TaskFragment newInstance(UUID uuid) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_USER_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        mRepositoryUser = TaskManagerDBRepository.getInstance(getActivity());
        mTasks = mRepositoryTask.getTaskList();
        mUsers = mRepositoryUser.getUserList();

        //this is storage of this Intent
        if (getArguments() != null) {
            mIdUser = (UUID) getArguments().getSerializable(ARGS_USER_ID);
            for (User user : mUsers) {
                if (user.getIDUser().equals(mIdUser))
                    mUser = user;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        setViews(view);
        initViews();
        setListener();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_menu, menu);
        MenuItem itemShow = menu.findItem(R.id.show_users);
        MenuItem itemDelete = menu.findItem(R.id.delete_all_task);
        if (userName.equals("admin")) {
            itemShow.setVisible(true);
            itemDelete.setVisible(false);
        } else {
            itemShow.setVisible(false);
            itemDelete.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_users:
                Intent intent = ListAllUsersActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_SHOW_USERS);
                return true;
            case R.id.delete_all_task:
                DeleteTasksFragment deleteTasksFragment = DeleteTasksFragment.newInstance(mIdUser);
                deleteTasksFragment.setTargetFragment(TaskFragment.this,
                        REQUEST_CODE_DELETE_ALL_TASKS);
                deleteTasksFragment.show(getActivity().
                        getSupportFragmentManager(), TAG_DELETE_ALL_TASKS);
                return true;
            case R.id.log_out:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViews(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager_state_task);
        mButtonAdd = view.findViewById(R.id.float_action_add);
    }

    private void setListener() {
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment = CreateTaskFragment.newInstance();
                createTaskFragment.setTargetFragment(TaskFragment.this,
                        REQUEST_CODE_CREATE_TASK);
                createTaskFragment.show(getActivity().getSupportFragmentManager(),
                        TAG_CREATE_TASK);
            }
        });
    }


    private void initViews() {
        userName = mUser.getUsername();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mUser.getUsername());
        mTasks = mRepositoryTask.getTaskList();
        setListTask();

    }

    public void setListTask() {
        if (mAdapter == null) {
            mViewPager.setAdapter(createCardAdapter());
            new TabLayoutMediator(mTabLayout, mViewPager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    String nameTab = String.valueOf(mStates.get(position));
                    tab.setText(nameTab);
                }
            }).attach();

        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    public class ViewPagerAdapter extends FragmentStateAdapter {


        public ViewPagerAdapter(@NonNull final FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            TaskListFragment taskListFragment = TaskListFragment.newInstance(position, mIdUser);
            return taskListFragment;
        }

        @Override
        public int getItemCount() {
            return mStates.size();
        }
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        return adapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_CREATE_TASK) {
            String title = data.getStringExtra(CreateTaskFragment.EXTRA_SEND_TITLE);
            String description = data.getStringExtra(CreateTaskFragment.EXTRA_SEND_DESCRIPTION);
            State state = (State) data.getSerializableExtra(CreateTaskFragment.EXTRA_SEND_STATE);
            Date date = (Date) data.getSerializableExtra(CreateTaskFragment.EXTRA_SEND_DATE);
            UUID uuid = (UUID) data.getSerializableExtra(CreateTaskFragment.EXTRA_TASK_ID);
            Uri uri = data.getParcelableExtra(CreateTaskFragment.EXTRA_SEND_URI);

            Task task = mRepositoryTask.getTask(uuid);
            task.setTitleTask(title);
            task.setDescription(description);
            task.setIdUser(mIdUser);
            task.setDateTask(date);
            task.setStateTask(state);
            if (uri!=null){
                task.setUri(uri);
            }
            mRepositoryTask.updateTask(task);
            mTasks = mRepositoryTask.getTaskList();
            setListTask();

        } else if (requestCode == REQUEST_CODE_DELETE_ALL_TASKS) {
            boolean check = data.getBooleanExtra(DeleteTasksFragment.EXTRA_SEND_CHECK_DELETE,
                    false);
            if (check) {
                mTasks = mRepositoryTask.getTaskList();
                setListTask();
            }
        } else if (requestCode== REQUEST_CODE_SHOW_USERS){
            mTasks = mRepositoryTask.getTaskList();
            setListTask();
        }
    }
}