package com.omgproduction.dsport_application.activities.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.CreatePostStartValues;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

public class CreatePostActivity extends AbstractFragmentActivity implements CreatePostStartValues {

    private String pinboardOwner;

    private int type;
    private Bitmap postPicture;

    private PostService postService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_create_post);

        preparePostData();

        findViewById(R.id.create_post_button).setOnClickListener(this);
        findViewById(R.id.create_post_camera_button).setOnClickListener(this);
        findViewById(R.id.create_post_gallery_button).setOnClickListener(this);
        postService = new PostService(this);

        User user = getLocalUser();

        setPic(R.id.create_post_picture, BitmapUtils.getBitmapFromString(CreatePostActivity.this,user.getPicture()));
        setText(R.id.create_post_username, user.getUsername());
    }

    @Override
    protected void removeAllErrors() {

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
            public void onFailure(String errorCode) {
                printError(R.id.activity_create_post, errorCode, R.string.retry, new View.OnClickListener() {
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
        pinboardOwner = getIntent().getStringExtra(ApplicationKeys.APPLICATION_POST_OWNER_ID);
        type = getIntent().getIntExtra(CREATE_POST_TYPE_KEY, CREATE_POST_TEXT_VALUE);

        switch (type){
            case CREATE_POST_PICTURE_VALUE:
                openCamera();
                break;
            case CREATE_POST_GALLERY_VALUE:
                openGallery();
        }
    }

    @Override
    protected void onBitmapResult(Bitmap bitmap) {
        type = CREATE_POST_PICTURE_VALUE;
        postPicture = bitmap;
        ((ImageView)findViewById(R.id.create_post_post_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_post_post_picture).setVisibility(View.VISIBLE);
    }
}
