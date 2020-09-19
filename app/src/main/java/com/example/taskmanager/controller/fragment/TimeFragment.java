package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;

import java.util.Calendar;
import java.util.Date;


public class TimeFragment extends DialogFragment{
    public static final String EXTRA_USER_SELECTED_TIME = "com.example.taskmanager.userSelectedTime";
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time, null);
        setViews(view);
        initTimePicker();
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity())
                .setView(view)

                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date userSelectedTime = extractTimeFromDatePicker();
                        sendResult(userSelectedTime);
                    }
                })
                .setNegativeButton(android.R.string.cancel,null);
        AlertDialog dialog =builder.create();
        return dialog;

    }

    private void sendResult(Date userSelectedTime) {
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_TIME, userSelectedTime);
        fragment.onActivityResult(requestCode, resultCode, intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Date extractTimeFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return calendar.getTime();
    }

    private void setViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker_task);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimePicker() {
        // i have a date and i want to set it in time picker.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);
    }




}