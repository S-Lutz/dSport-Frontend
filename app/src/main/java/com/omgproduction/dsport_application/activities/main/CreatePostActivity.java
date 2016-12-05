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
    private String postPicture;

    public static final int TEXT = 0;
    public static final int PICTURE = 1;
    public static final int GALLERY = 2;
    public static final String TYPE = "TYPE";

    private static final int CAM_REQUEST = 1;
    private static final int PIC_CROP = 2;
    private static final int SELECT_PICTURE = 3;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);
        getIntentValues();
        findViewById(R.id.create_post_button).setOnClickListener(this);

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
            case TEXT:
                postPicture = "";
                break;
            case PICTURE:
                capture();
                break;
            case GALLERY:
                gallery();
        }
    }
    private void capture() {
        try{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }catch(ActivityNotFoundException e){
            //display an error message
            printError(R.id.activity_create_post,"e5");
        }
    }

    private void gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_PICTURE);
    }
    private void performCrop(Uri uri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 2048);
            cropIntent.putExtra("outputY", 1080);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException e){
            //display an error message
            printError(R.id.activity_create_post,"e6");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            final Bundle extras = data.getExtras();
            if (requestCode == CAM_REQUEST || requestCode == SELECT_PICTURE) {

                //TODO FIX QUALITY OF BITMAP
                //temporaryOwnCrop(data.getData());
                performCrop(data.getData());
            }else if(requestCode == PIC_CROP){
                //TODO FIX QUALITY OF BITMAP
                Bitmap thePic = extras.getParcelable("data");
                Log.e("PIC",String.valueOf(thePic.getHeight()));
                Log.e("PIC",String.valueOf(thePic.getWidth()));
                ((ImageView)findViewById(R.id.create_post_post_picture)).setImageBitmap(thePic);
                findViewById(R.id.create_post_post_picture).setVisibility(View.VISIBLE);
            }
        }else{
            onBackPressed();
        }
    }
}
