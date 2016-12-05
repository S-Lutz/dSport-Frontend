package com.omgproduction.dsport_application.activities.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.StringUtils;
import com.omgproduction.dsport_application.utils.Transitions;

import org.json.JSONException;


public class ProfileActivity extends NavigationActivity{

    private FloatingActionButton fabEdit, fabGallery, fabCamera;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise, fabOpenBig;
    private boolean isEditFabOpen = false;


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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ((AppCompatImageView) findViewById(R.id.profile_pic)).setMaxHeight(size.x/2);

        getSupportActionBar().setTitle("");



        preferFloatingButtons();
        addActionListeners();


        loadLocalData();
        loadOnlineData();
    }

    private void loadLocalData() {
        UserController.getInstance().getLocalUser(context,new OnResultAdapter<User>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onSuccess(User user) {
                setText(R.id.profile_username_text,user.getUsername());
                setText(R.id.profile_email_text,user.getEmail());
                setText(R.id.profile_firstname_text,user.getFirstname());
                setText(R.id.profile_lastname_text,user.getLastname());
                setPic(R.id.profile_pic,user.getBitmap(context));
            }
        });

    }

    private void addActionListeners() {
        setRefresher((SwipeRefreshLayout)findViewById(R.id.profile_refresher));
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
        fabOpenBig = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open_big);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anti_clockwise);

        fabEdit.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabGallery.setOnClickListener(this);
    }

    private void loadOnlineData() {
        UserController.getInstance().getGlobalUser(this, new OnResultAdapter<User>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                setText(R.id.profile_username_text, user.getUsername());
                setText(R.id.profile_email_text, user.getEmail());
                setText(R.id.profile_firstname_text, user.getFirstname());
                setText(R.id.profile_lastname_text, user.getLastname());
                setPic(R.id.profile_pic,user.getBitmap(context));
            }

            @Override
            public void onConnectionError(VolleyError error) {
                error.printStackTrace();
            }

            @Override
            public void onBackendError(String ErrorCode) {
                //TODO Errorhandling
            }

            @Override
            public void onJSONException(JSONException e) {
                //TODO Errorhandling
                e.printStackTrace();
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });

    }

    @Override
    protected boolean onBackPressedAfterNavigationClosed() {
        return true;
    }

    @Override
    protected void removeAllErrors() {
        removeInputError(R.id.profile_email_input_layout);
        removeInputError(R.id.profile_password_input_layout);
        removeInputError(R.id.profile_firstname_input_layout);
        removeInputError(R.id.profile_lastname_input_layout);
        removeInputError(R.id.profile_password_confirm_input_layout);
        removeInputError(R.id.profile_username_input_layout);
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
        final String password1 = getTVText(R.id.profile_password_input1);
        final String password2 = getTVText(R.id.profile_password_input2);

        if(password1.trim().isEmpty()){
            printInputError(R.id.profile_password_input_layout,"e2");
        }
        if(password2.trim().isEmpty()){
            printInputError(R.id.profile_password_confirm_input_layout,"e2");
        }

        if(password1.equals(password2)){
            savePassword(password1);
            removeInputError(R.id.profile_password_input_layout);
            removeInputError(R.id.profile_password_confirm_input_layout);
        }else{
            printInputError(R.id.profile_password_input_layout,"e1");
            printInputError(R.id.profile_password_confirm_input_layout,"e1");
        }
    }

    private void performEmailConfirm() {
        final String email = getTVText(R.id.profile_email_input);
        final String currentEmail = Preferences.getInstance(this).getStringDetail(ApplicationKeys.EMAIL,"");
        if(!currentEmail.trim().isEmpty()){
            if(!email.trim().isEmpty()){
                if(!email.equals(currentEmail)){
                    if(StringUtils.isValidEmail(email)){
                        saveEmail(email);
                        removeInputError(R.id.profile_email_input_layout);
                    }else{
                        printInputError(R.id.profile_email_input_layout,"e3");
                    }
                }else{
                    printInputError(R.id.profile_email_input_layout,"e7");
                }
            }else{
                printInputError(R.id.profile_email_input_layout,"e2");
            }
        }else{
            //User not logged in
            SessionController.getInstance().logout(this);
        }
    }

    private void performLastnameConfirm() {
        String lastname = getTVText(R.id.profile_lastname_input);
        String currentLastname = Preferences.getInstance(this).getStringDetail(ApplicationKeys.LASTNAME,"");
        if(!currentLastname.trim().isEmpty()){
            if(!lastname.trim().isEmpty()){
                if(!lastname.equals(currentLastname)){
                    saveLastname(lastname);
                    removeInputError(R.id.profile_lastname_input_layout);
                }else{
                    printInputError(R.id.profile_lastname_input_layout,"e7");
                }
            }else{
                printInputError(R.id.profile_lastname_input_layout,"e2");
            }
        }else{
            //User not logged in
            SessionController.getInstance().logout(this);
        }
    }

    private void performFirstnameConfirm() {
        final String firstname = getTVText(R.id.profile_firstname_input);
        final String currentFirstname = Preferences.getInstance(this).getStringDetail(ApplicationKeys.FIRSTNAME,"");
        if(!currentFirstname.trim().isEmpty()){
            if(!firstname.trim().isEmpty()){
                if(!firstname.equals(currentFirstname)){
                    saveFirstname(firstname);
                    removeInputError(R.id.profile_firstname_input_layout);
                }else{
                    printInputError(R.id.profile_firstname_input_layout,"e7");
                }
            }else{
                printInputError(R.id.profile_firstname_input_layout,"e2");
            }
        }else{
            //User not logged in
            SessionController.getInstance().logout(this);
        }
    }

    private void performUsernameConfirm() {
        final String username = getTVText(R.id.profile_username_input);
        final String currentUsername = Preferences.getInstance(this).getStringDetail(ApplicationKeys.USERNAME,"");
        if(!currentUsername.trim().isEmpty()){
            if(!username.trim().isEmpty()){
                if(!username.equals(currentUsername)){
                    saveUsername(username);
                    removeInputError(R.id.profile_username_input_layout);
                }else{
                    printInputError(R.id.profile_username_input_layout,"e7");
                }
            }else{
                printInputError(R.id.profile_username_input_layout,"e2");
            }
        }else{
            //User not logged in
            SessionController.getInstance().logout(this);
        }
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


    private void savePicture(final Bitmap thePic) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.PICTURE, BitmapUtils.getStringFromBitmap(thePic), new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(String picString) {
                setPic(R.id.profile_pic,BitmapUtils.getBitmapFromString(context,picString));
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savePicture(thePic);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_container,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.profile_container,"e0");
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void saveEmail(final String email) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.EMAIL, email, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                removeInputError(R.id.profile_email_input_layout);
                showProgressBar(true);
            }

            @Override
            public void onSuccess(String emailString) {
                setText(R.id.profile_email_text,emailString);
                setText(R.id.profile_email_input,"");
                showEmail(false);
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveEmail(email);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_email_input_layout,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.profile_email_input_layout,"e0");
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void saveFirstname(final String firstname) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.FIRSTNAME, firstname, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
                removeInputError(R.id.profile_firstname_input_layout);
            }

            @Override
            public void onSuccess(String firstnameString) {
                setText(R.id.profile_firstname_text,firstnameString);
                setText(R.id.profile_firstname_input,"");
                showFirstname(false);
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveFirstname(firstname);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_firstname_input_layout,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.profile_firstname_input_layout,"e0");
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void saveLastname(final String lastname) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.LASTNAME, lastname, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
                removeInputError(R.id.profile_lastname_input_layout);
            }

            @Override
            public void onSuccess(String lastnameString) {
                setText(R.id.profile_lastname_text,lastnameString);
                setText(R.id.profile_lastname_input,"");
                showLastname(false);
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveLastname(lastname);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_lastname_input_layout,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.profile_lastname_input_layout,"e0");
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void savePassword(final String password) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.PASSWORD, password, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
                removeInputError(R.id.profile_password_input_layout);
                removeInputError(R.id.profile_password_confirm_input_layout);
            }

            @Override
            public void onSuccess(String result) {
                showPassword(false);
                setText(R.id.profile_password_input1,"");
                setText(R.id.profile_password_input2,"");
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savePassword(password);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_password_input_layout,errorCode);
                printError(R.id.profile_password_confirm_input_layout,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });

    }

    private void saveUsername(final String username) {
        UserController.getInstance().saveUserDetail(this, ApplicationKeys.USERNAME, username, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
                removeInputError(R.id.profile_username_input_layout);
            }

            @Override
            public void onSuccess(String usernameString) {
                setText(R.id.profile_username_text,usernameString);
                setText(R.id.profile_username_input,"");
                showUsername(false);
            }

            @Override
            public void onConnectionError(VolleyError error) {
                printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveUsername(username);
                    }
                });
            }

            @Override
            public void onBackendError(String errorCode) {
                printError(R.id.profile_username_input_layout,errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.profile_username_input_layout,"e0");
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(context);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });

    }

    private void performFabGalleryClick() {
        performFabClick();
        openGallery();
    }

    private void performFabCameraClick() {
        performFabClick();
        openCamera();
    }

    @Override
    protected void onBitmapResult(Bitmap bitmap) {
        savePicture(bitmap);
    }

    @Override
    public void onBackPressed() {
        if(isSomethingShown()){
            closeAll();
        }else{
            super.onBackPressed();
        }
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

    private void showPassword(boolean flag){
        if(flag){

            showAnimation(R.id.profile_password_edit, R.id.profile_password_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_edit_fab));
        }else if (isPasswordShown){
            hideAnimation(R.id.profile_password_edit, R.id.profile_password_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_edit_fab));
        }
        isPasswordShown = flag;

    }

    private void showUsername(boolean flag){
        if(flag){
            showAnimation(R.id.profile_username_edit, R.id.profile_username_input_container,
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }else if (isUsernameShown){
            hideAnimation(R.id.profile_username_edit, R.id.profile_username_input_container,
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }
        isUsernameShown = flag;
    }

    private void showFirstname(boolean flag){
        if(flag){
            showAnimation(R.id.profile_firstname_edit, R.id.profile_firstname_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }else if (isFirstnameShown){
            hideAnimation(R.id.profile_firstname_edit, R.id.profile_firstname_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }
        isFirstnameShown = flag;
    }

    private void showLastname(boolean flag){
        if(flag){
            showAnimation(R.id.profile_lastname_edit, R.id.profile_lastname_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }else if(isLastnameShown){
            hideAnimation(R.id.profile_lastname_edit, R.id.profile_lastname_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_email_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }
        isLastnameShown = flag;
    }

    private void showEmail(boolean flag){
        if(flag){
            showAnimation(R.id.profile_email_edit, R.id.profile_email_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }else if(isEmailShown){
            hideAnimation(R.id.profile_email_edit, R.id.profile_email_input_container,
                    findViewById(R.id.profile_username_edit),
                    findViewById(R.id.profile_firstname_edit),
                    findViewById(R.id.profile_lastname_edit),
                    findViewById(R.id.profile_password_edit),
                    findViewById(R.id.profile_edit_fab));
        }
        isEmailShown = flag;
    }

    private void showAnimation(int vMain, final int container, final View... vOthers){
        fabEdit.setAnimation(fabClose);
        fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                Transitions.slideOutLeft((ViewGroup) findViewById(R.id.profile_container),vOthers);
                Transitions.slideOutRight((ViewGroup) findViewById(R.id.profile_container), fabEdit);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Transitions.slideInBottom((ViewGroup) findViewById(R.id.profile_container),findViewById(container));
            }
        });
    }
    private void hideAnimation(final int vMain, final int container, final View... vOthers){

        Transitions.slideOutBottom((ViewGroup) findViewById(R.id.profile_container),findViewById(container));
        fabEdit.setAnimation(fabOpenBig);
        fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                Transitions.slideInLeft((ViewGroup) findViewById(R.id.profile_container),vOthers);
                Transitions.slideInRight((ViewGroup) findViewById(R.id.profile_container), fabEdit);
            }
        });
    }

    private boolean isSomethingShown(){
        return isEmailShown||isFirstnameShown||isLastnameShown||isUsernameShown||isPasswordShown;
    }
    //magic happens here
    private void closeAll(){
        showEmail(false);
        showLastname(false);
        showUsername(false);
        showPassword(false);
        showFirstname(false);
    }

    private Animation fadeInOut(View view){
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_in_out);
        view.startAnimation(fade);
        return fade;
    }

    @Override
    public void onRefresh() {
        loadOnlineData();
    }
}
