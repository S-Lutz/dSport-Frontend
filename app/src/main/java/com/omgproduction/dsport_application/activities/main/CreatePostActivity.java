package com.omgproduction.dsport_application.activities.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.controller.PostController;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AdvancedActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

public class CreatePostActivity extends AdvancedActivity {

    private String pinboardOwner;

    public static final int TEXT = 0;
    public static final int PICTURE = 1;
    public static final int GALLERY = 2;
    public static final String TYPE = "TYPE";

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);
        getIntentValues();
        findViewById(R.id.create_post_button).setOnClickListener(this);
        findViewById(R.id.create_post_camera_button).setOnClickListener(this);
        findViewById(R.id.create_post_gallery_button).setOnClickListener(this);

        UserController.getInstance().getLocalUser(this, new OnResultAdapter<User>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                setPic(R.id.create_post_picture, user.getBitmap(CreatePostActivity.this));
                setText(R.id.create_post_username, user.getUsername());
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(CreatePostActivity.this);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
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
        try{
            bmp = BitmapUtils.getStringFromBitmap(((BitmapDrawable)((ImageView)findViewById(R.id.create_post_post_picture)).getDrawable()).getBitmap());
        }catch (NullPointerException e){
            bmp = "";
            type = TEXT;
        }
        final String picture = bmp;


        UserController.getInstance().getLocalUser(this,new OnResultAdapter<User>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                pinboardOwner = (pinboardOwner==null)?user.getId():pinboardOwner;
                PostController.getInstance().createPost(user.getId(), pinboardOwner,(type == TEXT)?"":picture,text,title, new OnResultAdapter<Void>(){
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
                    public void onConnectionError(VolleyError e) {
                        printError(R.id.activity_create_post, "e100", R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                savePost();
                            }
                        });
                    }

                    @Override
                    public void onBackendError(String errorCode) {
                        printError(R.id.activity_create_post, errorCode, R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                savePost();
                            }
                        });
                    }

                    @Override
                    public void onUserNotFound() {
                        SessionController.getInstance().logout(CreatePostActivity.this);
                    }

                    @Override
                    public void onFinishQuery() {
                        showProgressBar(false);
                    }
                });

            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(CreatePostActivity.this);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    public void getIntentValues() {
        pinboardOwner = getIntent().getStringExtra(ApplicationKeys.OWNER_ID);
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
        ((ImageView)findViewById(R.id.create_post_post_picture)).setImageBitmap(bitmap);
        findViewById(R.id.create_post_post_picture).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCameraException(ActivityNotFoundException e) {
        printError(R.id.activity_create_post, "e6");
    }
}
