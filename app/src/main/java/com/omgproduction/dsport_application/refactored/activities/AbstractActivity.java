package com.omgproduction.dsport_application.refactored.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.helper.PictureUtil;

import java.io.File;
import java.io.IOException;

import static com.omgproduction.dsport_application.refactored.config.CameraOptions.SELECT_PICTURE;

public abstract class AbstractActivity extends AppCompatActivity {

    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected static final int REQUEST_IMAGE_CAPTURE = 1;
    protected String mCurrentPhotoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

    }

    protected void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE);
    }


    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = PictureUtil.createImageFile(this);
                mCurrentPhotoPath = photoFile.getPath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.omgproduction.dsport_application.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void setPic(Context context, View view) {
        ImageView v = (ImageView) findViewById(view.getId());
        v.setVisibility(View.VISIBLE);
        GlideApp
                .with(context)
                .load(new File(mCurrentPhotoPath))
                .centerCrop()
                .into(v);
    }

    public void deleteStoredPic(Boolean cameraRequest) {
        if (!mCurrentPhotoPath.isEmpty() && cameraRequest) {
            File imgFile = new File(mCurrentPhotoPath);
            imgFile.delete();
        }
        mCurrentPhotoPath = "";
    }

    @Override
    public void onBackPressed() {
        if (!mCurrentPhotoPath.isEmpty()) {
            File imgFile = new File(mCurrentPhotoPath);
            imgFile.delete();
            mCurrentPhotoPath = "";
        }
        super.onBackPressed();
    }

    public abstract int getLayout();

}