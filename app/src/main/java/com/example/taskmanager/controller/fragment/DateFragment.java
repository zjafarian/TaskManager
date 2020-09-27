package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateFragment extends DialogFragment {
    public static final String EXTRA_USER_SELECTED_DATE = "com.example.taskmanager.userSelectedDate";
    private Date mDateTask;
    private DatePicker mDatePicker;


    public static final String ARGS_DATE = "argsDate";

    public DateFragment() {
        // Required empty public constructor
    }

    public static DateFragment newInstance(Date date) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_DATE, date);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateTask = (Date) getArguments().getSerializable(ARGS_DATE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_date, null);
        setViews(view);
        initViews();

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date userSelectedDate = extractDateFromDatePicker();
                        sendResult(userSelectedDate);


                    }
                })
                .setNegativeButton(android.R.string.cancel,null);
        AlertDialog dialog =builder.create();

        return dialog;
    }

    private void sendResult(Date userSelectedDate) {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_DATE, userSelectedDate);
        fragment.onActivityResult(requestCode, resultCode, intent);

    }

    private void setViews(View view) {
        mDatePicker = view.findViewById(R.id.date_picker_task);
    }

    private Date extractDateFromDatePicker() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();
        GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        return calendar.getTime();
    }
    private void initViews() {
        initDatePicker();
    }
    private void initDatePicker() {
        // i have a date and i want to set it in date picker.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDateTask);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, null);
    }
}