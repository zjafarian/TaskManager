package com.example.taskmanager.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.google.android.material.textfield.TextInputEditText;


public class EditTaskFragment extends Fragment {
    private Task mTask;
    private IRepositoryTask mRepositoryTask;
    private TextInputEditText mTextTitleEdit;
    private TextInputEditText mTextDescriptionEdit;



    public EditTaskFragment() {
        // Required empty public constructor
    }


    public static EditTaskFragment newInstance(Task task) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }
}