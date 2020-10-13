package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.TaskManagerDBRepository;

import java.util.List;
import java.util.UUID;


public class DeleteTasksFragment extends DialogFragment {

    public static final String ARGS_USER_ID = "userId";
    public static final String EXTRA_SEND_CHECK_DELETE = "com.example.taskmanager.sendCheckDelete";
    private UUID mUserId;
    private IRepositoryTask mRepositoryTask;
    private List<Task> mTasks;
    private boolean mCheckDelete;


    public DeleteTasksFragment() {
        // Required empty public constructor
    }


    public static DeleteTasksFragment newInstance(UUID uuid) {
        DeleteTasksFragment fragment = new DeleteTasksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_USER_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        mTasks = mRepositoryTask.getTaskList();
        if (getArguments() != null) {
            mUserId = (UUID) getArguments().getSerializable(ARGS_USER_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //View view = inflater.inflate(R.layout.fragment_edit_task, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_edit)
                .setMessage(R.string.delete_tasks)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Task taskFind : mTasks) {
                            if (taskFind.getIdUser().equals(mUserId))
                                mRepositoryTask.deleteTask(taskFind);
                        }
                        mCheckDelete=true;
                        sendResult();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCheckDelete=false;
                        sendResult();
                        dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_CHECK_DELETE,mCheckDelete);
        fragment.onActivityResult(requestCode, resultCode, intent);
    }
}