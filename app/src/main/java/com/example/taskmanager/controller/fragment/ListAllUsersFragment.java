package com.example.taskmanager.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskManagerDBRepository;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ListAllUsersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private IRepositoryTask mRepositoryTask;
    private IRepositoryUser mRepositoryUser;
    private List<Task> mTasks;
    private List<User> mUsers;
    private UserAdapter mUserAdapter;
    private List<User> mUserListForAdapter = new ArrayList<>();


    public ListAllUsersFragment() {
        // Required empty public constructor
    }


    public static ListAllUsersFragment newInstance() {
        ListAllUsersFragment fragment = new ListAllUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        mRepositoryUser = TaskManagerDBRepository.getInstance(getActivity());
        mTasks = mRepositoryTask.getTaskList();
        mUsers = mRepositoryUser.getUserList();
        for (User userFind:mUsers) {
            if (!userFind.getUsername().equals("admin") && !userFind.getPassword().equals("admin"))
                mUserListForAdapter.add(userFind);

        }

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_all_users,
                container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mUserAdapter == null) {
            mUserAdapter = new ListAllUsersFragment.UserAdapter(mUserListForAdapter);
            mRecyclerView.setAdapter(mUserAdapter);

        } else {
            mUserAdapter.setUsersAdapter(mUserListForAdapter);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_all_task_list);
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        private MaterialTextView mTextViewUserName;
        private MaterialTextView mTextViewDateRegister;
        private MaterialTextView mTextViewNumberOfTasks;
        private Button mButtonDeleteUser;
        private User mUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
            setListener();

        }

        private void setListener() {
            mButtonDeleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Task taskFind : mTasks) {
                        if (taskFind.getIdUser().equals(mUser.getIDUser()))
                            mRepositoryTask.deleteTask(taskFind);

                    }
                    mRepositoryUser.deleteUser(mUser);
                    mUsers = mRepositoryUser.getUserList();
                    mUserListForAdapter.clear();
                    for (User userFind:mUsers) {
                        if (!userFind.getUsername().equals("admin") && !userFind.getPassword().equals("admin"))
                            mUserListForAdapter.add(userFind);

                    }
                    initViews();
                }
            });
        }

        private void findViews(@NonNull View itemView) {
            mTextViewUserName = itemView.findViewById(R.id.textView_userName);
            mTextViewDateRegister = itemView.findViewById(R.id.textView_date_register);
            mTextViewNumberOfTasks = itemView.findViewById(R.id.textView_number_tasks);
            mButtonDeleteUser = itemView.findViewById(R.id.btn_delete_user);
        }

        public void bindUser(User user) {
            int counter = 0;
            mUser = user;
            mTextViewUserName.setText(user.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:SS");
            String date = simpleDateFormat.format(user.getDate());
            date = getString(R.string.registration_date, date);
            mTextViewDateRegister.setText(date);
            for (Task taskFind : mTasks) {
                if (taskFind.getIdUser().equals(user.getIDUser()))
                    counter++;
            }
            String number = getString(R.string.number_tasks, String.valueOf(counter));
            mTextViewNumberOfTasks.setText(number);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<ListAllUsersFragment.UserHolder> {
        private List<User> mUsersAdapter;

        public UserAdapter(List<User> usersAdapter) {
            mUsersAdapter = usersAdapter;
        }

        public List<User> getUsersAdapter() {
            return mUsersAdapter;
        }

        public void setUsersAdapter(List<User> usersAdapter) {
            mUsersAdapter = usersAdapter;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.row_user, parent, false);
            ListAllUsersFragment.UserHolder userHolder = new ListAllUsersFragment.UserHolder(view);
            return userHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            User user = mUsersAdapter.get(position);
            if (!user.getUsername().equals("admin") && !user.getPassword().equals("admin"))
                holder.bindUser(user);
        }

        @Override
        public int getItemCount() {
            return mUsersAdapter.size();
        }
    }
}