package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.activity.LoginActivity;
import com.example.taskmanager.controller.activity.SignUpActivity;
import com.example.taskmanager.controller.activity.TaskListActivity;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.taskmanager.controller.activity.LoginActivity.EXTRA_REPOSITORY_USER_LOGIN;

public class LoginFragment extends Fragment {
    public static final int REQUEST_CODE_SIGNUP = 0;
    public static final String ARGS_REPOSITORY_USER_LOGIN = "args_repository_user_login";
    public static final String SAVE_USER_NAME_LOGIN = "saveUserNameLogin";
    public static final String SAVE_PASSWORD_LOGIN = "savePasswordLogin";
    public static final String SAVE_REPOSITORY_USER_LOGIN = "saveRepositoryUserLogin";

    private Button mButton_logIn;
    private Button mButton_signUp;
    private TextInputEditText mUsernameLogin;
    private TextInputEditText mPasswordLogin;
    private String username;
    private String password;
    private IRepositoryUser mRepositoryUser;
    private List<User> mUsers;


    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(IRepositoryUser repositoryUser) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_REPOSITORY_USER_LOGIN, repositoryUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsers = new ArrayList<>();
        mRepositoryUser = UserRepository.getInstance();
        mUsers = mRepositoryUser.getUserList();
        User user = new User("admin", "admin");
        mUsers.add(user);
        mRepositoryUser.insertUser(user);

        //this is storage of this fragment
        mRepositoryUser = (IRepositoryUser) getArguments().getSerializable(ARGS_REPOSITORY_USER_LOGIN);
        mUsers = mRepositoryUser.getUserList();

        //Handel saveInstance
        if (savedInstanceState !=null){
            username = savedInstanceState.getString(SAVE_USER_NAME_LOGIN);
            password = savedInstanceState.getString(SAVE_PASSWORD_LOGIN);
            mRepositoryUser = (IRepositoryUser) savedInstanceState.getSerializable(SAVE_REPOSITORY_USER_LOGIN);
            mUsers = mRepositoryUser.getUserList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setId(view);
        initViews();
        if (savedInstanceState!=null){
            initViews();
        }
        setListener();
        return view;
    }

    private void initViews() {
        mUsernameLogin.setText(username);
        mPasswordLogin.setText(password);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_SIGNUP) {
            mRepositoryUser = (IRepositoryUser) data.getSerializableExtra(EXTRA_REPOSITORY_USER_LOGIN);
            mUsers = mRepositoryUser.getUserList();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_USER_NAME_LOGIN,username);
        outState.putString(SAVE_PASSWORD_LOGIN,password);
        outState.putSerializable(SAVE_REPOSITORY_USER_LOGIN,mRepositoryUser);
    }

    private void setId(View view) {
        mUsernameLogin = view.findViewById(R.id.username_login);
        mPasswordLogin = view.findViewById(R.id.password_login);
        mButton_logIn = view.findViewById(R.id.btn_login);
        mButton_signUp = view.findViewById(R.id.btn_signup);

    }

    private void setListener() {
        mButton_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mButton_signUp.isClickable()) {
                    Toast toast = Toast.makeText(getActivity(), R.string.message_not_sign_up,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                } else {
                    String user = mUsernameLogin.getText().toString();
                    String pass = mPasswordLogin.getText().toString();
                    if (user.matches("") || pass.matches("")) {
                        Toast toast = Toast.makeText(getActivity(), R.string.message_signup,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else {
                        //Intent intent = TaskListActivity.newIntent(getActivity(), mRepositoryUser, username,password);
                        //startActivity(intent);
                    }

                }
            }


        });

        mButton_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsernameLogin.getText().toString();
                password = mPasswordLogin.getText().toString();
                Intent intent = SignUpActivity.newIntent(getActivity(), mRepositoryUser, username, password);
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);

            }
        });
    }
}