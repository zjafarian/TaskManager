package com.example.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.fragment.LoginFragment;
import com.example.taskmanager.controller.fragment.SignUpFragment;
import com.example.taskmanager.repository.IRepositoryUser;

import java.util.UUID;

public class LoginActivity extends SingleFragmentActivity {

    public static final String EXTRA_REPOSITORY_USER_LOGIN = "com.example.taskmanager.repositoryUserLogin";
    public static final String EXTRA_ID_USER = "com.example.taskmanager.idUser";

    public static Intent newIntent(Context context, IRepositoryUser repositoryUser, UUID id) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_REPOSITORY_USER_LOGIN,repositoryUser);
        intent.putExtra(EXTRA_ID_USER, id);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        IRepositoryUser repositoryUser = (IRepositoryUser) getIntent().getSerializableExtra(EXTRA_REPOSITORY_USER_LOGIN);
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_ID_USER);
        LoginFragment loginFragment = LoginFragment.newInstance(repositoryUser, id);
        return loginFragment;

    }

}