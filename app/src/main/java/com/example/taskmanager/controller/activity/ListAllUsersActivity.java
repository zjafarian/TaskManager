package com.example.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.controller.fragment.ListAllUsersFragment;

public class ListAllUsersActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ListAllUsersActivity.class);
        return intent;
    }



    @Override
    public Fragment createFragment() {
        ListAllUsersFragment listAllUsersFragment = ListAllUsersFragment.newInstance();
        return listAllUsersFragment;
    }
}