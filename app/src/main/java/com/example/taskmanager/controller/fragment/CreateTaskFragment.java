package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;


public class CreateTaskFragment extends DialogFragment {

    public static final String ARGS_ID_USER = "argsIdUser";
    public static final int REQUEST_CODE_DATE_CREATE = 0;
    public static final String DATE_PICKER_CREATE = "datePickerCreate";
    public static final int REQUEST_CODE_TIME_CREATE = 1;
    public static final String TIME_PICKER_CREATE = "timePickerCreate";
    public static final String EXTRA_SEND_TITLE = "com.example.taskmanager.send title";
    public static final String EXTRA_SEND_DESCRIPTION = "com.example.taskmanager.send description";
    public static final String EXTRA_SEND_STATE = "com.example.taskmanager.send state";
    public static final String EXTRA_SEND_DATE = "com.example.taskmanager.send date";
    public static final String ARGS_USER_ID = "UserId";
    private TextInputEditText mTextTitleCreate;
    private TextInputEditText mTextDescriptionCreate;
    private Button mButtonDateCreate;
    private Button mButtonTimeCreate;
    private RadioGroup mRadioGroupCreate;
    private Date mDate = new Date();
    private Date mTime = new Date();
    private State mStateTask;
    private Date mDateNew = new Date();
    private String mTitle;
    private String mDescription;


    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_create_task, null);
        setViews(view);
        setListener();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_create)
                .setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createTask();
                        sendResult();

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private void sendResult() {

        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_TITLE, mTitle);
        intent.putExtra(EXTRA_SEND_DESCRIPTION, mDescription);
        intent.putExtra(EXTRA_SEND_STATE, mStateTask);
        intent.putExtra(EXTRA_SEND_DATE, mDateNew);
        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void createTask() {
        switch (mRadioGroupCreate.getCheckedRadioButtonId()) {
            case R.id.rd_btn_done_create:
                mStateTask = State.Done;
                break;
            case R.id.rd_btn_doing_create:
                mStateTask = State.Doing;
                break;
            case R.id.rd_btn_todo_create:
                mStateTask = State.Todo;
                break;
        }

        mTitle = mTextTitleCreate.getText().toString();
        mDescription = mTextDescriptionCreate.getText().toString();
        /*Task task = new Task(mTitle,mDescription,mStateTask,mUserId);
        task.setDateTask(mDateNew);
        mIRepositoryTask.insertTask(task);*/
    }

    private void setListener() {
        mButtonDateCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFragment dateFragment = DateFragment.newInstance(mDate);
                dateFragment.setTargetFragment
                        (CreateTaskFragment.this, REQUEST_CODE_DATE_CREATE);
                dateFragment.show(getActivity().getSupportFragmentManager(), DATE_PICKER_CREATE);
            }
        });

        mButtonTimeCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeFragment timeFragment = TimeFragment.newInstance(mDate);
                timeFragment.setTargetFragment
                        (CreateTaskFragment.this, REQUEST_CODE_TIME_CREATE);
                timeFragment.show(getActivity().getSupportFragmentManager(), TIME_PICKER_CREATE);
            }
        });


    }

    private void setViews(View view) {
        mTextTitleCreate = view.findViewById(R.id.title_task_input_create);
        mTextDescriptionCreate = view.findViewById(R.id.description_task_input_create);
        mButtonDateCreate = view.findViewById(R.id.btn_date_create);
        mButtonTimeCreate = view.findViewById(R.id.btn_time_create);
        mRadioGroupCreate = view.findViewById(R.id.radio_group_create);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_DATE_CREATE) {
            mDate = (Date) data.getSerializableExtra(DateFragment.EXTRA_USER_SELECTED_DATE);
            updateDateTask(mDate);
            mDate = setCalender();
        }
        if (requestCode == REQUEST_CODE_TIME_CREATE) {
            mTime = (Date) data.getSerializableExtra(TimeFragment.EXTRA_USER_SELECTED_TIME);
            updateTimeTask(mTime);
            mTime = setCalender();
        }
        mDateNew = setCalender();

    }

    private Date setCalender() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(mTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private void updateDateTask(Date dateUserSelected) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUserSelected);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mButtonDateCreate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);

    }

    private void updateTimeTask(Date timeUserSelected) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeUserSelected);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mButtonTimeCreate.setText(hour + ":" + minute + ":" + second);
    }

}