package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.CreatePostStartValues;
import com.omgproduction.dsport_application.config.ErrorCodes;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

public class CreatePostActivity extends AbstractFragmentActivity implements CreatePostStartValues {

    private String pinboardOwner;

    private int type;
    private Bitmap postPicture;

    private UserService userService;
    private PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_create_post);

        preparePostData();

        findViewById(R.id.create_post_button).setOnClickListener(this);
        findViewById(R.id.create_post_camera_button).setOnClickListener(this);
        findViewById(R.id.create_post_gallery_button).setOnClickListener(this);

        userService = new UserService(this);
        postService = new PostService(this);

        setPic(R.id.create_post_picture, userService.getLocalUser().getBitmap(CreatePostActivity.this));
        setText(R.id.create_post_username, userService.getLocalUser().getUsername());
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
        postService.createPost(user.getId(), pinboardOwner,(type == TEXT)?"":picture,text,title, new RequestFuture<Void>(){
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
        type = getIntent().getIntExtra(TYPE,TEXT);

        switch (type){
            case PICTURE:
                openCamera();
                break;
            case GALLERY:
                openGallery();
        }
    }

    @Override
    protected void onBitmapResult(Bitmap bitmap) {
        type = PICTURE;
        postPicture = bitmap;
        ((ImageView)findViewById(R.id.create_post_post_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_post_post_picture).setVisibility(View.VISIBLE);
    }
}
