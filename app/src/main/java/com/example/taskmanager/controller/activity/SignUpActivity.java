package com.example.taskmanager.controller.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.taskmanager.controller.fragment.SignUpFragment;
import com.example.taskmanager.repository.IRepositoryUser;

public class SignUpActivity extends SingleFragmentActivity {

    public static final String EXTRA_REPOSITORY_USER_SIGN_UP = "com.example.taskmanager.repository_user";
    public static final String EXTRA_USERNAME_SIGN_UP = "com.example.taskmanager.UsernameSignUp";
    public static final String EXTRA_PASSWORD_SIGN_UP = "com.example.taskmanager.PasswordSignUp";

    public static Intent newIntent(Context context, IRepositoryUser repositoryUser, String username, String password) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_REPOSITORY_USER_SIGN_UP, repositoryUser);
        intent.putExtra(EXTRA_USERNAME_SIGN_UP, username);
        intent.putExtra(EXTRA_PASSWORD_SIGN_UP, password);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        IRepositoryUser repositoryUser = (IRepositoryUser) getIntent().getSerializableExtra(EXTRA_REPOSITORY_USER_SIGN_UP);
        String userName = getIntent().getStringExtra(EXTRA_USERNAME_SIGN_UP);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD_SIGN_UP);

        SignUpFragment signUpFragment = SignUpFragment.newInstance(repositoryUser, userName, password);
        return signUpFragment;
    }


}