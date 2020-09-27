package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.controller.fragment.LoginFragment;

import java.util.UUID;

public class LoginActivity extends SingleFragmentActivity {


    public static final String EXTRA_ID_USER = "com.example.taskmanager.idUser";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(EXTRA_ID_USER, id);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_ID_USER);
        LoginFragment loginFragment = LoginFragment.newInstance(id);
        return loginFragment;

    }

}