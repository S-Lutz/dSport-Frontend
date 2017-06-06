package com.omgproduction.dsport_application.activities.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.CreatePostStartValues;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.File;
import java.io.IOException;

public class CreatePostActivity extends AbstractFragmentActivity implements CreatePostStartValues {

    private String pinboardOwner;

    private int type;
    private Bitmap postPicture;

    private PostService postService;
    private boolean firstStarted = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preparePostData();

        findViewById(R.id.create_post_button).setOnClickListener(this);
        findViewById(R.id.create_post_camera_button).setOnClickListener(this);
        findViewById(R.id.create_post_gallery_button).setOnClickListener(this);
        postService = new PostService(this);

        User user = getLocalUser();

        setPic(R.id.create_post_picture, BitmapUtils.getBitmapFromString(user.getPicture()));
        setText(R.id.create_post_username, user.getUsername());
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public int getLayout() {
        return R.layout.layout_activity_create_post;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_post_button:
                savePost();
                break;
            case R.id.create_post_camera_button:
                openCamera();
                break;
            case R.id.create_post_gallery_button:
                openGallery();
                break;
        }
    }

    private void savePost() {
        final String text = ((EditText)findViewById(R.id.create_post_text)).getText().toString();
        final String title = ((EditText)findViewById(R.id.create_post_title)).getText().toString();
        String bmp;
        if(postPicture==null){
            bmp = "";
        }else {
            bmp = BitmapUtils.getStringFromBitmap(postPicture);
        }
        final String picture = bmp;

        User user = userService.getLocalUser();
        pinboardOwner = (pinboardOwner==null)?user.getId():pinboardOwner;
        postService.createPost(user.getId(), pinboardOwner,(type == CREATE_POST_TEXT_VALUE)?"":picture,text,title, new RequestFuture<Void>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Void result) {
                CreatePostActivity.super.onBackPressed();
                CreatePostActivity.this.finish();
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printError(R.id.activity_create_post, errorMessage, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savePost();
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    public void preparePostData() {
        pinboardOwner = getIntent().getStringExtra(INTENT_POST_OWNER_ID);
        type = getIntent().getIntExtra(CREATE_POST_TYPE_KEY, CREATE_POST_TEXT_VALUE);

        if(!firstStarted) return;
        firstStarted = false;
        switch (type){
            case CREATE_POST_PICTURE_VALUE:
                openCamera();
                break;
            case CREATE_POST_GALLERY_VALUE:
                openGallery();
                break;
        }
    }

    public void onCameraResult(Bitmap bitmap, File file) {
        type = CREATE_POST_PICTURE_VALUE;
        postPicture = bitmap;
        ((ImageView)findViewById(R.id.create_post_post_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_post_post_picture).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CREATE_POST_TYPE_KEY)) {
                type = savedInstanceState.getInt(CREATE_POST_TYPE_KEY);
            }
            if(savedInstanceState.containsKey(CREATE_POST_FIRST_START_KEY)){
                firstStarted = savedInstanceState.getBoolean(CREATE_POST_FIRST_START_KEY);
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(CREATE_POST_TYPE_KEY, type);
        outState.putBoolean(CREATE_POST_FIRST_START_KEY, firstStarted);


        super.onSaveInstanceState(outState);
    }


    //TMP

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CAM_REQUEST || requestCode == SELECT_PICTURE) {
                openCrop(getUriFromData(data));
            }else if(requestCode == PIC_CROP){
                onCameraResult(getBitmapFromData( data), new File(getUriFromData(data).getPath()));
            }
        }else {
        }
    }
    public Bitmap getBitmapFromData(Intent data){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getUriFromData(data));
            return Bitmap.createScaledBitmap(bitmap, CAMERA_DEFAULT_CAPTURE_WIDTH, CAMERA_DEFAULT_CAPTURE_HEIGHT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Uri getUriFromData(Intent data){
        return data.getData();
    }

    public void openCrop(Uri uri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("openCrop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", CAMERA_DEFAULT_CAPTURE_WIDTH);
            cropIntent.putExtra("outputY", CAMERA_DEFAULT_CAPTURE_HEIGHT);
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openCamera() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAM_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE);
    }


}
