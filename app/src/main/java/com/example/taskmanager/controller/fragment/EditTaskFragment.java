package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.TaskManagerDBRepository;
import com.example.taskmanager.utils.PictureUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class EditTaskFragment extends DialogFragment {
    public static final String DATE_PICKER_EDIT = "datePickerEdit";
    public static final String TIME_PICKER_EDIT = "timePickerEdit";
    public static final int REQUEST_CODE_DATE_EDIT = 0;
    public static final int REQUEST_CODE_TIME_EDIT = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;
    public static final String ARGS_TASK_ID = "TaskId";
    public static final String EXTRA_SEND_STATE = "com.example.taskmanager.sendState";
    public static final String EXTRA_SEND_CHECK_DELETE = "com.example.taskmanager.sendCheckDelete";
    public static final String EXTRA_SEND_STATE_DELETE =
            "com.example.taskmanager.send_state_delete";
    public static final String AUTHORITY = "com.example.taskmanager.fileProvider";
    public static final String SELECT_IMAGE_TAG = "SelectImage";
    private Task mTask;
    private IRepositoryTask mRepositoryTask;
    private TextInputEditText mTextTitleEdit;
    private TextInputEditText mTextDescriptionEdit;
    private Button mBtnDate;
    private Button mBtnTime;
    private RadioGroup mRdBtnGroupEdit;
    private Button mBtnEdit;
    private RadioButton mRdBtnDoneEdit;
    private RadioButton mRdBtnDoingEdit;
    private RadioButton mRdBtnTodoEdit;
    private Date mDate = new Date();
    private Date mTime = new Date();
    private UUID mTaskId;
    private List<Task> mTaskList;
    private Date mDateNew = new Date();
    private State mState;
    private boolean mCheckDelete = false;
    private State mStateDelete;
    private Button mBtnShare;
    private Button mButtonImageTask;
    private ImageView mImageViewTask;
    private File mPhotoFile;


    public EditTaskFragment() {
        // Required empty public constructor
    }


    public static EditTaskFragment newInstance(UUID uuid) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TASK_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        mTaskList = mRepositoryTask.getTaskList();
        if (getArguments() != null) {
            mTaskId = (UUID) getArguments().getSerializable(ARGS_TASK_ID);
        }
        for (Task taskFind : mTaskList) {
            if (taskFind.getIdTask().equals(mTaskId))
                mTask = taskFind;
        }
        mDateNew = setCalender();
        mPhotoFile = mRepositoryTask.getPhotoFile(mTask);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_edit_task, null);
        setViews(view);
        initViews();
        setListener();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_edit)
                .setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTask();
                        updateTask();
                        sendResult();
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStateDelete = mTask.getStateTask();
                        mRepositoryTask.deleteTask(mTask);
                        mCheckDelete = true;
                        sendResult();
                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;

    }

    private void editTask() {
        mTask.setTitleTask(mTextTitleEdit.getText().toString());
        mTask.setDescription(mTextDescriptionEdit.getText().toString());
        mTask.setDateTask(mDateNew);
        mTask.setStateTask(mState);

    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_STATE, mState);
        intent.putExtra(EXTRA_SEND_CHECK_DELETE, mCheckDelete);
        intent.putExtra(EXTRA_SEND_STATE_DELETE, mStateDelete);
        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void updateTask() {
        mRepositoryTask.updateTask(mTask);
    }


    private void setListener() {
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnable(true);
            }
        });
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFragment dateFragment = DateFragment.newInstance(mTask.getDateTask());
                dateFragment.setTargetFragment
                        (EditTaskFragment.this, REQUEST_CODE_DATE_EDIT);
                dateFragment.show(getActivity().getSupportFragmentManager(), DATE_PICKER_EDIT);

            }
        });
        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeFragment timeFragment = TimeFragment.newInstance(mTask.getDateTask());
                timeFragment.setTargetFragment
                        (EditTaskFragment.this, REQUEST_CODE_TIME_EDIT);
                timeFragment.show(getActivity().getSupportFragmentManager(), TIME_PICKER_EDIT);

            }
        });
        mRdBtnDoingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = State.Doing;
            }
        });

        mRdBtnDoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = State.Done;
            }
        });

        mRdBtnTodoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = State.Todo;
            }
        });
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReportIntent();

            }
        });

        mButtonImageTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageFragment selectImageFragment =
                        SelectImageFragment.newInstance(mTask.getIdTask());
                selectImageFragment.setTargetFragment
                        (EditTaskFragment.this, REQUEST_CODE_SELECT_IMAGE);
                selectImageFragment.show(
                        getActivity().
                                getSupportFragmentManager(),
                        SELECT_IMAGE_TAG);
            }
        });

    }

    private void shareReportIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getReport());
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_share_subject));
        sendIntent.setType("text/plain");

        Intent shareIntent =
                Intent.createChooser(sendIntent, getString(R.string.send_report));

        //we prevent app from crash if the intent has no destination.
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(shareIntent);
    }

    private String getReport() {
        String report;
        String title = mTextTitleEdit.getText().toString();
        String description = mTextDescriptionEdit.getText().toString();
        String state = "";
        if (mRdBtnDoingEdit.isChecked())
            state = "Doing";
        else if (mRdBtnDoneEdit.isChecked())
            state = "Done";
        else if (mRdBtnTodoEdit.isChecked())
            state = "Todo";
        String date = mBtnDate.getText().toString();
        String time = mBtnTime.getText().toString();
        report = "Your Task is \n" + "Title: " + title + "\n" + "Description: " + description
                + "\n" + "Date: " + date + ":" + time + "\n" + "State: " + state;
        return report;
    }

    private void setViews(View view) {
        mTextTitleEdit = view.findViewById(R.id.title_task_input_edit);
        mTextDescriptionEdit = view.findViewById(R.id.description_task_input_edit);
        mBtnDate = view.findViewById(R.id.btn_date_edit);
        mBtnTime = view.findViewById(R.id.btn_time_edit);
        mRdBtnGroupEdit = view.findViewById(R.id.radio_group_edit);
        mRdBtnDoingEdit = view.findViewById(R.id.rd_btn_doing_edit);
        mRdBtnDoneEdit = view.findViewById(R.id.rd_btn_done_edit);
        mRdBtnTodoEdit = view.findViewById(R.id.rd_btn_todo_edit);
        mBtnEdit = view.findViewById(R.id.btn_task_edit);
        mBtnShare = view.findViewById(R.id.btn_share);
        mButtonImageTask = view.findViewById(R.id.btn_take_image_edit);
        mImageViewTask = view.findViewById(R.id.img_view_task_edit);
    }

    private void initViews() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTask.getDateTask());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        mBtnDate.setText(year + "/" + month + "/" + dayOfMonth);
        mBtnTime.setText(hour + ":" + minute);
        mTextTitleEdit.setText(mTask.getTitleTask());
        mTextDescriptionEdit.setText(mTask.getDescription());
        switch (mTask.getStateTask()) {
            case Done:
                mRdBtnDoneEdit.setChecked(true);
                break;
            case Doing:
                mRdBtnDoingEdit.setChecked(true);
                break;
            case Todo:
                mRdBtnTodoEdit.setChecked(true);
                break;
        }
        mState = mTask.getStateTask();

        updatePhotoView();
        setEnable(false);

    }

    private void setEnable(boolean checkEnable) {
        if (!checkEnable) {
            mTextTitleEdit.setEnabled(false);
            mTextDescriptionEdit.setEnabled(false);
            mBtnTime.setEnabled(false);
            mBtnDate.setEnabled(false);
            mRdBtnTodoEdit.setEnabled(false);
            mRdBtnDoneEdit.setEnabled(false);
            mRdBtnDoingEdit.setEnabled(false);
            mButtonImageTask.setEnabled(false);
        } else {
            mTextTitleEdit.setEnabled(true);
            mTextDescriptionEdit.setEnabled(true);
            mBtnTime.setEnabled(true);
            mBtnDate.setEnabled(true);
            mRdBtnTodoEdit.setEnabled(true);
            mRdBtnDoneEdit.setEnabled(true);
            mRdBtnDoingEdit.setEnabled(true);
            mButtonImageTask.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_DATE_EDIT) {
            mDate = (Date) data.getSerializableExtra(DateFragment.EXTRA_USER_SELECTED_DATE);
            updateDateTask(mDate);
            mDate = setCalender();
        }
        if (requestCode == REQUEST_CODE_TIME_EDIT) {
            mTime = (Date) data.getSerializableExtra(TimeFragment.EXTRA_USER_SELECTED_TIME);
            updateDateTask(mTime);
            mTime = setCalender();
        }
        if (requestCode==REQUEST_CODE_SELECT_IMAGE){
            mPhotoFile = mRepositoryTask.getPhotoFile(mTask);
            boolean check = data.getBooleanExtra
                    (SelectImageFragment.EXTRA_SEND_CHECK_DEVICE_OR_CAMERA, false);
            if (check) {
                Uri photoUri = generateUriForPhotoFile();
                getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhotoView();
            } else {
                Uri uri = data.getParcelableExtra(SelectImageFragment.EXTRA_SEND_URI_IMAGE);
                getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                mImageViewTask.setImageURI(uri);
            }
        }

    }

    private void updateDateTask(Date dateUserSelected) {
        mTask.setDateTask(dateUserSelected);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUserSelected);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mBtnDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        mBtnTime.setText(hour + ":" + minute + ":" + second);

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


    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists())
            return;

        //this has a better memory management.
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
        mImageViewTask.setImageBitmap(bitmap);
    }

    private Uri generateUriForPhotoFile() {
        return FileProvider.getUriForFile(
                getContext(),
                AUTHORITY,
                mPhotoFile);
    }

}