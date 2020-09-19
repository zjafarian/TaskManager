package com.example.taskmanager.controller.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.taskmanager.R;

import java.util.Date;


public class TimeFragment extends DialogFragment {
    private Date mTime;
    private TimePicker mTimePicker;


    public static final String ARGS_TIME = "argsTime";

    public TimeFragment() {
        // Required empty public constructor
    }


    public static TimeFragment newInstance(Date time) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TIME,time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTime= (Date) getArguments().getSerializable(ARGS_TIME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time, null);
        setViews(view);
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date userSelectedTime = extractDateFromTimePicker();
                        sendResult(userSelectedTime);
                    }
                })
                .setNegativeButton(android.R.string.cancel,null);
        AlertDialog dialog =builder.create();

    }

    private void setViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker_task);
    }


}