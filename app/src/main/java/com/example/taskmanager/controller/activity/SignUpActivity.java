package com.example.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.taskmanager.R;
import com.example.taskmanager.controller.fragment.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new SignUpFragment();
    }


}