package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.activity.LoginActivity;
import com.example.taskmanager.controller.activity.SignUpActivity;
import com.example.taskmanager.controller.activity.TaskActivity;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.TaskManagerDBRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginFragment extends Fragment {

    public static final String ARGS_REPOSITORY_USER_LOGIN = "args_repository_user_login";
    public static final String SAVE_USER_NAME_LOGIN = "saveUserNameLogin";
    public static final String SAVE_PASSWORD_LOGIN = "savePasswordLogin";
    public static final String ARGS_ID_USER = "argsIdUser";
    public static final String SAVE_ID_USER = "save_id_user";
    public static final int REQUEST_CODE_SING_UP = 0;
    public static final String TAG_ID_USER = "TagIdUser";
    private Button mButton_logIn;
    private Button mButton_signUp;
    private TextInputEditText mUsernameLogin;
    private TextInputEditText mPasswordLogin;
    private String username = "";
    private String password = "";
    private IRepositoryUser mRepositoryUser;
    private List<User> mUsers = new ArrayList<>();
    private UUID mUserId;
    private boolean mCheck;


    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(UUID id) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_ID_USER, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryUser = TaskManagerDBRepository.getInstance(getActivity());
        mUsers = mRepositoryUser.getUserList();

        //this is storage of this fragment
        if (getArguments() != null) {
            mUserId = (UUID) getArguments().getSerializable(ARGS_ID_USER);
            for (User user1 : mUsers) {
                if (user1.getIDUser().equals(mUserId)) {
                    username = user1.getUsername();
                    password = user1.getPassword();
                }
            }
        }


        //Handel saveInstance
        if (savedInstanceState != null) {
            username = savedInstanceState.getString(SAVE_USER_NAME_LOGIN);
            password = savedInstanceState.getString(SAVE_PASSWORD_LOGIN);
            mUsers = mRepositoryUser.getUserList();
            mUserId = (UUID) savedInstanceState.getSerializable(SAVE_ID_USER);
            for (User user1 : mUsers) {
                if (user1.getIDUser().equals(mUserId)) {
                    username = user1.getUsername();
                    password = user1.getPassword();
                }
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setId(view);

        if (savedInstanceState != null) {
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_USER_NAME_LOGIN, username);
        outState.putString(SAVE_PASSWORD_LOGIN, password);
        outState.putSerializable(SAVE_ID_USER, mUserId);
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
                username = mUsernameLogin.getText().toString().trim();
                password = mPasswordLogin.getText().toString().trim();
                mUsers = mRepositoryUser.getUserList();

                if (username.matches("") || password.matches("")) {
                    Toast toast = Toast.makeText(getActivity(), R.string.message_signup,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                } else {
                    for (User userFind : mUsers) {
                        if (userFind.getUsername().equals(username)
                                && userFind.getPassword().equals(password)) {
                            mUserId = userFind.getIDUser();
                            mCheck = true;
                        }
                    }
                    if (mCheck) {
                        Intent intent =
                                TaskActivity.newIntent(getActivity(), mUserId);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getActivity(),
                                "username or password is wrong", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            }
        });

        mButton_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsernameLogin.getText().toString().trim();
                password = mPasswordLogin.getText().toString().trim();
                Intent intent = SignUpActivity.newIntent(getActivity(), username, password);
                startActivityForResult(intent, REQUEST_CODE_SING_UP);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK && data == null)
            return;
        if (requestCode == REQUEST_CODE_SING_UP) {
            mUsers = mRepositoryUser.getUserList();
            mUserId = (UUID) data.getSerializableExtra(LoginActivity.EXTRA_ID_USER);

        }
    }
}