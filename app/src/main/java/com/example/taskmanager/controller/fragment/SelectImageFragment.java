package com.example.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.IRepositoryTask;
import com.example.taskmanager.repository.TaskManagerDBRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SelectImageFragment extends DialogFragment {

    public static final String AUTHORITY = "com.example.taskmanager.fileProvider";
    public static final String TAG = "TaskManager";
    public static final String ARG_UUID_TASK = "uuidTask";
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 0;
    public static final String EXTRA_SEND_CHECK_DEVICE_OR_CAMERA =
            "com.example.taskmanager.SendCheckDeviceOrCamera";
    public static final String EXTRA_SEND_URI_IMAGE = "com.example.taskmanager.sendUriImage";
    public static final int REQUEST_CODE_SELECT_FROM_GALLERY = 1;
    private Button mButtonDevice;
    private Button mButtonCamera;
    private File mPhotoFile;
    private boolean mCheckDeviceOrCamera;
    private IRepositoryTask mRepositoryTask;
    private Uri mUri;


    public SelectImageFragment() {
        // Required empty public constructor
    }


    public static SelectImageFragment newInstance(UUID uuid) {
        SelectImageFragment fragment = new SelectImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID_TASK, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryTask = TaskManagerDBRepository.getInstance(getActivity());
        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID_TASK);
        Task task = mRepositoryTask.getTask(uuid);
        mPhotoFile = mRepositoryTask.getPhotoFile(task);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_select_image, null);
        setViews(view);
        setListener();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_create)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult();
                    }
                });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    private void setViews(View view) {
        mButtonDevice = view.findViewById(R.id.btn_select_device);
        mButtonCamera = view.findViewById(R.id.btn_select_camera);
    }

    private void setListener() {
        mButtonDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromDevice();
                mCheckDeviceOrCamera = false;
            }
        });

        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromCamera();
                mCheckDeviceOrCamera = true;
            }
        });
    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_CHECK_DEVICE_OR_CAMERA, mCheckDeviceOrCamera);
        intent.putExtra(EXTRA_SEND_URI_IMAGE, mUri);
        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void selectImageFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (mPhotoFile != null && takePictureIntent
                    .resolveActivity(getActivity().getPackageManager()) != null) {

                // file:///data/data/com.example.ci/files/234234234234.jpg
                mUri = generateUriForPhotoFile();

                grantWriteUriToAllResolvedActivities(takePictureIntent, mUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);

            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private Uri generateUriForPhotoFile() {
        return FileProvider.getUriForFile(
                getContext(),
                AUTHORITY,
                mPhotoFile);
    }

    private void grantWriteUriToAllResolvedActivities(Intent takePictureIntent, Uri photoUri) {
        List<ResolveInfo> activities = getActivity().getPackageManager()
                .queryIntentActivities(
                        takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : activities) {
            getActivity().grantUriPermission(
                    activity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private void selectImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // file:///data/data/com.example.ci/files/234234234234.jpg
        Uri photoUri = generateUriForPhotoFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_SELECT_FROM_GALLERY) {
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    mUri = item.getUri();
                }
            } else if (data.getData() != null) {
                mUri = data.getData();






            }

        }


    }


}