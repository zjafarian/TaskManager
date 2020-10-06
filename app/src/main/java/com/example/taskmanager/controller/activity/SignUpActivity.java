package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.controller.fragment.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivity {


    public static final String EXTRA_USERNAME_SIGN_UP = "com.example.taskmanager.UsernameSignUp";
    public static final String EXTRA_PASSWORD_SIGN_UP = "com.example.taskmanager.PasswordSignUp";

    public static Intent newIntent(Context context, String username, String password) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_USERNAME_SIGN_UP, username);
        intent.putExtra(EXTRA_PASSWORD_SIGN_UP, password);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        String userName = getIntent().getStringExtra(EXTRA_USERNAME_SIGN_UP);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD_SIGN_UP);
        SignUpFragment signUpFragment = SignUpFragment.newInstance(userName, password);
        return signUpFragment;
    }


}