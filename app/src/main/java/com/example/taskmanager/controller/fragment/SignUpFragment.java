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
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.activity.LoginActivity;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.IRepositoryUser;
import com.example.taskmanager.repository.UserRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignUpFragment extends Fragment {
    public static final String SAVE_USER_NAME_SIGN_UP = "saveUserNameSignUp";
    public static final String SAVE_PASSWORD_SIGN_UP = "savePasswordSignUp";
    public static final String ARGS_USERNAME_SING_UP = "argsUsernameSingUp";
    public static final String ARGS_PASSWORD_SIGN_UP = "argsPasswordSignUp";
    public static final String SAVE_REPOSITORY_USER_SIGN_UP = "SaveRepositoryUserSignUp";
    public static final String EXTRA_USER_ID_SIGN_UP = "userIdSignUp";
    private Button mSign;
    private TextInputEditText mUserNameSignUp;
    private TextInputEditText mPasswordSignUp;
    private String username;
    private String password;
    private IRepositoryUser mRepositoryUser;
    private List<User> mUserList = new ArrayList<>();
    private UUID mUserId;


    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String username,
                                             String password) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_USERNAME_SING_UP, username);
        args.putString(ARGS_PASSWORD_SIGN_UP, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryUser = UserRepository.getInstance();
        //this is storage of this fragment
        password = getArguments().getString(ARGS_PASSWORD_SIGN_UP);
        username = getArguments().getString(ARGS_USERNAME_SING_UP);
        mUserList = mRepositoryUser.getUserList();


        //Handel SaveInstance
        if (savedInstanceState != null) {
            username = savedInstanceState.getString(SAVE_USER_NAME_SIGN_UP);
            password = savedInstanceState.getString(SAVE_PASSWORD_SIGN_UP);
            mRepositoryUser = (IRepositoryUser) savedInstanceState.getSerializable(SAVE_REPOSITORY_USER_SIGN_UP);
            mUserList = mRepositoryUser.getUserList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setId(view);
        initViews();
        if (savedInstanceState != null) {
            initViews();
        }
        setListener();
        return view;
    }

    private void initViews() {
        mUserNameSignUp.setText(username);
        mPasswordSignUp.setText(password);
    }


    private void setId(View view) {
        mUserNameSignUp = view.findViewById(R.id.username_signUp);
        mPasswordSignUp = view.findViewById(R.id.password_signUp);
        mSign = view.findViewById(R.id.btn_signup_sign);
    }

    private void setListener() {
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUserNameSignUp.getText().toString();
                password = mPasswordSignUp.getText().toString();
                if (username.matches("") || password.matches("")) {
                    Toast toast = Toast.makeText(getActivity(),
                            R.string.message_signup, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                } else {
                    User user = new User(username, password);
                    mUserList.add(user);
                    mRepositoryUser.insertUser(user);
                    mUserId = user.getIDUser();
                    Intent intent = LoginActivity.newIntent(getActivity(),mUserId);
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }


            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_USER_NAME_SIGN_UP, username);
        outState.putString(SAVE_PASSWORD_SIGN_UP, password);
    }
}