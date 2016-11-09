package com.omgproduction.dsport_application.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.exceptions.UserNotFoundException;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.ConnectionUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends NavigationActivity {

    private AppCompatImageView pic;
    private FloatingActionButton fabEdit, fabGallery, fabCamera;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    private boolean isEditFabOpen = false;
    private static final int CAM_REQUEST = 1;
    private static final int PIC_CROP = 2;
    private static final int SELECT_PICTURE = 3;


    private boolean isUsernameShown = false;
    private boolean isLastnameShown = false;
    private boolean isFirstnameShown = false;
    private boolean isEmailShown = false;
    private boolean isPasswordShown = false;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {

        return R.layout.activity_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pic = (AppCompatImageView) findViewById(R.id.profile_pic);
        pic.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        pic.setMaxHeight(size.x/2);

        getSupportActionBar().setTitle("");


        preferFloatingButtons();
        addActionListeners();


        loadLocalData();
        //loadOnlineData();
    }

    private void loadLocalData() {
        showProgressBar(R.id.contentPanel,R.id.progress_bar);
        showProgressBar(pic,R.id.progress_bar_pic);

        Preferences preferences = Preferences.getInstance(context);

        ((TextView)findViewById(R.id.profile_username_text)).setText(preferences.getStringDetail(Keys.USERNAME,""));
        ((TextView)findViewById(R.id.profile_email_text)).setText(preferences.getStringDetail(Keys.EMAIL,""));
        ((TextView)findViewById(R.id.profile_firstname_text)).setText(preferences.getStringDetail(Keys.FIRSTNAME,""));
        ((TextView)findViewById(R.id.profile_lastname_text)).setText(preferences.getStringDetail(Keys.LASTNAME,""));
        String bitmapString = preferences.getStringDetail(Keys.PICTURE,"");
        if(bitmapString.isEmpty()){
            pic.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        }else{
            pic.setImageBitmap(BitmapUtils.getBitmapFromString(preferences.getStringDetail(Keys.PICTURE,"")));
        }
        hideProgressBar(R.id.contentPanel,R.id.progress_bar);
        hideProgressBar(pic,R.id.progress_bar_pic);
    }

    private void addActionListeners() {
        findViewById(R.id.profile_firstname_edit).setOnClickListener(this);
        findViewById(R.id.profile_lastname_edit).setOnClickListener(this);
        findViewById(R.id.profile_username_edit).setOnClickListener(this);
        findViewById(R.id.profile_email_edit).setOnClickListener(this);
        findViewById(R.id.profile_password_edit).setOnClickListener(this);
        findViewById(R.id.profile_firstname_confirm).setOnClickListener(this);
        findViewById(R.id.profile_lastname_confirm).setOnClickListener(this);
        findViewById(R.id.profile_username_confirm).setOnClickListener(this);
        findViewById(R.id.profile_email_confirm).setOnClickListener(this);
        findViewById(R.id.profile_password_confirm).setOnClickListener(this);
    }

    private void preferFloatingButtons() {
        fabEdit = (FloatingActionButton) findViewById(R.id.profile_edit_fab);
        fabCamera = (FloatingActionButton) findViewById(R.id.profile_edit_fab_camera);
        fabGallery = (FloatingActionButton) findViewById(R.id.profile_edit_fab_gallery);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anti_clockwise);

        fabEdit.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabGallery.setOnClickListener(this);
    }

    private void loadOnlineData() {
        showProgressBar(R.id.contentPanel,R.id.progress_bar);
        showProgressBar(pic,R.id.progress_bar_pic);

        try {
            UserController.getInstance().getUser(this, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(ConnectionUtils.Success(jsonObject)){
                        //TODO Fill Fields
                        JSONObject user = ConnectionUtils.extractJSONValue(jsonObject);
                        SessionController.getInstance().saveLocalUser(context,user);
                        try {
                            ((TextView)findViewById(R.id.profile_username_text)).setText(user.getString(Keys.USERNAME));
                            ((TextView)findViewById(R.id.profile_email_text)).setText(user.getString(Keys.EMAIL));
                            ((TextView)findViewById(R.id.profile_firstname_text)).setText(user.getString(Keys.FIRSTNAME));
                            ((TextView)findViewById(R.id.profile_lastname_text)).setText(user.getString(Keys.LASTNAME));
                            String bitmapString = user.getString(Keys.PICTURE);
                            if(bitmapString.isEmpty()){
                                pic.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                            }else{
                                pic.setImageBitmap(BitmapUtils.getBitmapFromString(user.getString(Keys.PICTURE)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        //TODO Errorhandling
                    }
                    hideProgressBar(R.id.contentPanel,R.id.progress_bar);
                    hideProgressBar(pic,R.id.progress_bar_pic);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    hideProgressBar(R.id.contentPanel,R.id.progress_bar);
                    //TODO Errorhandling
                }
            });
        } catch (UserNotFoundException e) {
            SessionController.getInstance().logoutUser(this);
        }

    }

    @Override
    protected boolean onBackPressedAfterNavigationClosed() {
        return true;
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_edit_fab: performFabClick(); break;
            case R.id.profile_edit_fab_camera: performFabCameraClick(); break;
            case R.id.profile_edit_fab_gallery: performFabGalleryClick(); break;
            case R.id.profile_username_edit: performUsernameClick(); break;
            case R.id.profile_firstname_edit: performFirstnameClick(); break;
            case R.id.profile_lastname_edit: performLastnameClick(); break;
            case R.id.profile_email_edit: performEmailClick(); break;
            case R.id.profile_password_edit: performPasswordClick(); break;
            case R.id.profile_username_confirm: performUsernameConfirm(); break;
            case R.id.profile_firstname_confirm: performFirstnameConfirm(); break;
            case R.id.profile_lastname_confirm: performLastnameConfirm(); break;
            case R.id.profile_email_confirm: performEmailConfirm(); break;
            case R.id.profile_password_confirm: performPasswordConfirm(); break;
        }
    }

    private void performPasswordConfirm() {
        //TODO confirm
    }

    private void performEmailConfirm() {
        //TODO confirm
    }

    private void performLastnameConfirm() {
        //TODO confirm
    }

    private void performFirstnameConfirm() {
        //TODO confirm
    }

    private void performUsernameConfirm() {
        //TODO confirm
    }

    private void performPasswordClick() {
        if(!isSomethingShown()){
            showPassword(true);
        }else if(isPasswordShown){
            showPassword(false);
        }
    }

    private void performEmailClick() {
        if(!isSomethingShown()){
            showEmail(true);
        }else if(isEmailShown){
            showEmail(false);
        }
    }

    private void performLastnameClick() {
        if(!isSomethingShown()){
            showLastname(true);
        }else if(isLastnameShown){
            showLastname(false);
        }
    }

    private void performFirstnameClick() {
        if(!isSomethingShown()){
            showFirstname(true);
        }else if(isFirstnameShown){
            showFirstname(false);
        }
    }

    private void performUsernameClick() {
        if(!isSomethingShown()){
            showUsername(true);
        }else if(isUsernameShown){
            showUsername(false);
        }
    }

    private void performFabCameraClick() {
        performFabClick();
        try{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }catch(ActivityNotFoundException e){
            //display an error message
            printError(R.id.profile_container,"e5");
        }
    }

    private void showPassword(boolean flag){
        isPasswordShown = flag;
        if(flag){
            fadeInOut(findViewById(R.id.profile_password_edit)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animateOut(findViewById(R.id.profile_username_edit));
                    animateOut(findViewById(R.id.profile_firstname_edit));
                    animateOut(findViewById(R.id.profile_lastname_edit));
                    animateOut(findViewById(R.id.profile_email_edit));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goInDown(findViewById(R.id.profile_password_input_container)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById(R.id.profile_password_input_container).setVisibility( View.VISIBLE);
                        }
                    });
                }
            });
        }else{
            goOutUp(findViewById(R.id.profile_password_input_container)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.profile_password_input_container).setVisibility( View.GONE);
                    fadeInOut(findViewById(R.id.profile_password_edit)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateIn(findViewById(R.id.profile_username_edit));
                            animateIn(findViewById(R.id.profile_firstname_edit));
                            animateIn(findViewById(R.id.profile_lastname_edit));
                            animateIn(findViewById(R.id.profile_email_edit));
                        }
                    });
                }
            });
        }

    }

    private void showUsername(boolean flag){
        isUsernameShown = flag;
        if(flag){
            fadeInOut(findViewById(R.id.profile_username_edit)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animateOut(findViewById(R.id.profile_firstname_edit));
                    animateOut(findViewById(R.id.profile_lastname_edit));
                    animateOut(findViewById(R.id.profile_email_edit));
                    animateOut(findViewById(R.id.profile_password_edit));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goInDown(findViewById(R.id.profile_username_input_container)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById(R.id.profile_username_input_container).setVisibility( View.VISIBLE);
                        }
                    });
                }
            });
        }else{
            goOutUp(findViewById(R.id.profile_username_input_container)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.profile_username_input_container).setVisibility( View.GONE);
                    fadeInOut(findViewById(R.id.profile_username_edit)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateIn(findViewById(R.id.profile_firstname_edit));
                            animateIn(findViewById(R.id.profile_lastname_edit));
                            animateIn(findViewById(R.id.profile_email_edit));
                            animateIn(findViewById(R.id.profile_password_edit));
                        }
                    });
                }
            });
        }
    }

    private void showFirstname(boolean flag){
        isFirstnameShown = flag;
        if(flag){
            fadeInOut(findViewById(R.id.profile_firstname_edit)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animateOut(findViewById(R.id.profile_username_edit));
                    animateOut(findViewById(R.id.profile_lastname_edit));
                    animateOut(findViewById(R.id.profile_email_edit));
                    animateOut(findViewById(R.id.profile_password_edit));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goInDown(findViewById(R.id.profile_firstname_input_container)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById(R.id.profile_firstname_input_container).setVisibility( View.VISIBLE);
                        }
                    });
                }
            });
        }else{
            goOutUp(findViewById(R.id.profile_firstname_input_container)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.profile_firstname_input_container).setVisibility( View.GONE);
                    fadeInOut(findViewById(R.id.profile_firstname_edit)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateIn(findViewById(R.id.profile_username_edit));
                            animateIn(findViewById(R.id.profile_lastname_edit));
                            animateIn(findViewById(R.id.profile_email_edit));
                            animateIn(findViewById(R.id.profile_password_edit));
                        }
                    });
                }
            });
        }
    }

    private void showLastname(boolean flag){
        isLastnameShown = flag;
        if(flag){
            fadeInOut(findViewById(R.id.profile_lastname_edit)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animateOut(findViewById(R.id.profile_username_edit));
                    animateOut(findViewById(R.id.profile_firstname_edit));
                    animateOut(findViewById(R.id.profile_email_edit));
                    animateOut(findViewById(R.id.profile_password_edit));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goInDown(findViewById(R.id.profile_lastname_input_container)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById(R.id.profile_lastname_input_container).setVisibility( View.VISIBLE);
                        }
                    });
                }
            });
        }else{
            goOutUp(findViewById(R.id.profile_lastname_input_container)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.profile_lastname_input_container).setVisibility( View.GONE);
                    fadeInOut(findViewById(R.id.profile_lastname_edit)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateIn(findViewById(R.id.profile_username_edit));
                            animateIn(findViewById(R.id.profile_firstname_edit));
                            animateIn(findViewById(R.id.profile_email_edit));
                            animateIn(findViewById(R.id.profile_password_edit));
                        }
                    });
                }
            });
        }
    }

    private void showEmail(boolean flag){
        isEmailShown = flag;
        if(flag){
            fadeInOut(findViewById(R.id.profile_email_edit)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animateOut(findViewById(R.id.profile_username_edit));
                    animateOut(findViewById(R.id.profile_firstname_edit));
                    animateOut(findViewById(R.id.profile_lastname_edit));
                    animateOut(findViewById(R.id.profile_password_edit));
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    goInDown(findViewById(R.id.profile_email_input_container)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            findViewById(R.id.profile_email_input_container).setVisibility( View.VISIBLE);
                        }
                    });
                }
            });
        }else{
            goOutUp(findViewById(R.id.profile_email_input_container)).setAnimationListener(new AnimationAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.profile_email_input_container).setVisibility( View.GONE);
                    fadeInOut(findViewById(R.id.profile_email_edit)).setAnimationListener(new AnimationAdapter() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateIn(findViewById(R.id.profile_username_edit));
                            animateIn(findViewById(R.id.profile_firstname_edit));
                            animateIn(findViewById(R.id.profile_lastname_edit));
                            animateIn(findViewById(R.id.profile_password_edit));
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == CAM_REQUEST || requestCode == SELECT_PICTURE) {
                performCrop(data.getData());
            }else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                savePicture(thePic);
            }
        }


    }

    private void savePicture(Bitmap thePic) {
        showProgressBar(pic,R.id.progress_bar_pic);
        try{
            UserController.getInstance().savePicture(this, BitmapUtils.getStringFromBitmap(thePic), new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        try {
                            String picString = jsonObject.getString(Keys.PICTURE);
                            pic.setImageBitmap(BitmapUtils.getBitmapFromString(picString));
                            Preferences.getInstance(context)
                                    .putString(Keys.PICTURE,picString)
                                    .commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //TODO Errorhandling
                        }
                    }else {
                        //TODO Errorhandling
                    }
                    hideProgressBar(pic, R.id.progress_bar_pic);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //TODO Errorhandling
                    hideProgressBar(pic, R.id.progress_bar_pic);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }
    }

    private void performCrop(Uri uri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException e){
            //display an error message
            printError(R.id.profile_container,"e6");
        }
    }

    private void performFabGalleryClick() {
        performFabClick();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_PICTURE);
    }

    private void performFabClick() {
        if(isEditFabOpen){
            fabCamera.startAnimation(fabClose);
            fabGallery.startAnimation(fabClose);
            fabEdit.setAnimation(fabClockWise);
            fabGallery.setClickable(false);
            fabCamera.setClickable(false);
            isEditFabOpen = false;
        }else {
            fabCamera.startAnimation(fabOpen);
            fabGallery.startAnimation(fabOpen);
            fabEdit.setAnimation(fabAntiClockWise);
            fabGallery.setClickable(true);
            fabCamera.setClickable(true);
            isEditFabOpen = true;
        }
    }

    private boolean isSomethingShown(){
        return isEmailShown||isFirstnameShown||isLastnameShown||isUsernameShown||isPasswordShown;
    }

    private void closeAll(){
        showLastname(false);
        showUsername(false);
        showEmail(false);
        showFirstname(false);
        showPassword(false);
    }
    private void animateOut(final View view){
        Animation goOut = AnimationUtils.loadAnimation(this,R.anim.go_out);
        goOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(goOut);
    }
    private void animateIn(final View view){
        Animation goIn = AnimationUtils.loadAnimation(this,R.anim.go_in);
        goIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(goIn);
    }

    private Animation fadeInOut(View view){
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_in_out);
        view.startAnimation(fade);
        return fade;
    }

    private Animation goInDown(final View view){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.go_in_up);
        view.startAnimation(anim);
        return anim;
    }

    private Animation goOutUp(final View view){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.go_out_down);
        view.startAnimation(anim);
        return anim;
    }
}
