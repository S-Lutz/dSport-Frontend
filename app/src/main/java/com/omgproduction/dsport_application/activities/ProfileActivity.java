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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.exceptions.UserNotFoundException;
import com.omgproduction.dsport_application.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.ConnectionUtils;
import com.omgproduction.dsport_application.utils.StringUtils;

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
        loadOnlineData();
    }

    private void loadLocalData() {
        showProgressBar(R.id.contentPanel,R.id.progress_bar);
        showProgressBar(pic,R.id.progress_bar_pic);


        User user = User.getInstance();
        setText(R.id.profile_username_text,user.getUsername());
        setText(R.id.profile_email_text,user.getEmail());
        setText(R.id.profile_firstname_text,user.getFirstname());
        setText(R.id.profile_lastname_text,user.getLastname());

        pic.setImageBitmap(user.getBitmap(context));
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
        UserController.getInstance().getUser(this, new OnResultListener<User>() {
            @Override
            public void onStart() {
                showProgressBar(R.id.contentPanel,R.id.progress_bar);
                showProgressBar(pic,R.id.progress_bar_pic);
            }

            @Override
            public void onSuccess(User user) {
                setText(R.id.profile_username_text, user.getUsername());
                setText(R.id.profile_email_text, user.getEmail());
                setText(R.id.profile_firstname_text, user.getFirstname());
                setText(R.id.profile_lastname_text, user.getLastname());
                pic.setImageBitmap(user.getBitmap(context));
            }

            @Override
            public void onConnectionError(VolleyError error) {
                error.printStackTrace();
            }

            @Override
            public void onBackendError(String ErrorCode, String ErrorString) {
                //TODO Errorhandling
            }

            @Override
            public void onJSONError(JSONException e) {
                //TODO Errorhandling
                e.printStackTrace();
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logoutUser(context);
            }

            @Override
            public void onResult() {
                hideProgressBar(R.id.contentPanel, R.id.progress_bar);
                hideProgressBar(pic, R.id.progress_bar_pic);
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
        final String currentEmail = Preferences.getInstance(this).getStringDetail(Keys.EMAIL,"");
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
            SessionController.getInstance().logoutUser(this);
            startLoginActivity(this);
        }
    }

    private void performLastnameConfirm() {
        String lastname = getTVText(R.id.profile_lastname_input);
        String currentLastname = Preferences.getInstance(this).getStringDetail(Keys.LASTNAME,"");
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
            SessionController.getInstance().logoutUser(this);
            startLoginActivity(this);
        }
    }

    private void performFirstnameConfirm() {
        final String firstname = getTVText(R.id.profile_firstname_input);
        final String currentFirstname = Preferences.getInstance(this).getStringDetail(Keys.FIRSTNAME,"");
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
            SessionController.getInstance().logoutUser(this);
            startLoginActivity(this);
        }
    }

    private void performUsernameConfirm() {
        final String username = getTVText(R.id.profile_username_input);
        final String currentUsername = Preferences.getInstance(this).getStringDetail(Keys.USERNAME,"");
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
            SessionController.getInstance().logoutUser(this);
            startLoginActivity(this);
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

    private void showPassword(boolean flag){
        isPasswordShown = flag;
        if(flag){
            showAnimation(R.id.profile_password_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_password_input_container);
        }else{
            hideAnimation(R.id.profile_password_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_password_input_container);
        }

    }

    private void showUsername(boolean flag){
        isUsernameShown = flag;
        if(flag){
            showAnimation(R.id.profile_username_edit,new int[]{
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_username_input_container);
        }else{
            hideAnimation(R.id.profile_username_edit,new int[]{
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_username_input_container);
        }
    }

    private void showFirstname(boolean flag){
        isFirstnameShown = flag;
        if(flag){
            showAnimation(R.id.profile_firstname_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_firstname_input_container);
        }else{
            hideAnimation(R.id.profile_firstname_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_firstname_input_container);
        }
    }

    private void showLastname(boolean flag){
        isLastnameShown = flag;
        if(flag){
            showAnimation(R.id.profile_lastname_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_lastname_input_container);
        }else{
            hideAnimation(R.id.profile_lastname_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_email_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_lastname_input_container);
        }
    }

    private void showEmail(boolean flag){
        isEmailShown = flag;
        if(flag){
            showAnimation(R.id.profile_email_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_email_input_container);
        }else{
            hideAnimation(R.id.profile_email_edit,new int[]{
                    R.id.profile_username_edit,
                    R.id.profile_firstname_edit,
                    R.id.profile_lastname_edit,
                    R.id.profile_password_edit,
                    R.id.profile_edit_fab
            }, R.id.profile_email_input_container);
        }
    }

    private void showAnimation(int vMain, final int[] vOthers, final int container){
        fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                for(int id :vOthers){
                    animateOut(findViewById(id));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goInDown(findViewById(container)).setAnimationListener(new AnimationAdapter() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        findViewById(container).setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
    private void hideAnimation(final int vMain, final int[] vOthers, final int container){
        goOutUp(findViewById(container)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(container).setVisibility( View.GONE);
                fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        for(int id : vOthers){
                            animateIn(findViewById(id));
                        }
                    }
                });
            }
        });
    }


    private void savePicture(final Bitmap thePic) {
        showProgressBar(pic,R.id.progress_bar_pic);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.PICTURE, BitmapUtils.getStringFromBitmap(thePic), new OnResultListener<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String picString) {
                    pic.setImageBitmap(BitmapUtils.getBitmapFromString(context,picString));
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
                public void onBackendError(String ErrorCode, String ErrorString) {

                }

                @Override
                public void onJSONError(JSONException e) {
                    e.printStackTrace();
                    printError(R.id.profile_container,"e0");
                }

                @Override
                public void onUserNotFound() {
                    SessionController.getInstance().logoutUser(context);
                }

                @Override
                public void onResult() {
                    hideProgressBar(pic, R.id.progress_bar_pic);
                }
            }new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        try {

                        } catch (JSONException e) {
                        }
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                    }
                    hideProgressBar(pic, R.id.progress_bar_pic);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savePicture(thePic);
                        }
                    });
                    hideProgressBar(pic, R.id.progress_bar_pic);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }
    }

    private void saveEmail(final String email) {
        showProgressBar(R.id.profile_email_input_container,R.id.progress_bar_email);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.EMAIL, email, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        removeInputError(R.id.profile_email_input_layout);
                        try {
                            String emailString = ConnectionUtils.extractJSONValue(jsonObject).getString(Keys.EMAIL);
                            setText(R.id.profile_email_text,emailString);
                            UserController.getInstance().update(context,Keys.EMAIL,emailString);
                            showEmail(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printError(R.id.profile_email_input_layout,"e0");
                        }
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                        printError(R.id.profile_email_input_layout,errorCode);

                    }
                    hideProgressBar(R.id.profile_email_input_container,R.id.progress_bar_email);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveEmail(email);
                        }
                    });
                    hideProgressBar(R.id.profile_email_input_container,R.id.progress_bar_email);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }
    }

    private void saveFirstname(final String firstname) {
        showProgressBar(R.id.profile_firstname_input_container,R.id.progress_bar_firstname);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.FIRSTNAME, firstname, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        try {
                            removeInputError(R.id.profile_firstname_input_layout);
                            String firstnameString = ConnectionUtils.extractJSONValue(jsonObject).getString(Keys.FIRSTNAME);
                            setText(R.id.profile_firstname_text,firstnameString);
                            UserController.getInstance().update(context,Keys.FIRSTNAME,firstnameString);
                            showFirstname(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printError(R.id.profile_firstname_input_layout,"e0");
                        }
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                        printError(R.id.profile_firstname_input_layout,errorCode);
                    }
                    hideProgressBar(R.id.profile_firstname_input_container,R.id.progress_bar_firstname);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveFirstname(firstname);
                        }
                    });
                    hideProgressBar(R.id.profile_firstname_input_container,R.id.progress_bar_firstname);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }
    }

    private void saveLastname(final String lastname) {
        showProgressBar(R.id.profile_lastname_input_container,R.id.progress_bar_lastname);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.LASTNAME, lastname, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        try {
                            String lastnameString = ConnectionUtils.extractJSONValue(jsonObject).getString(Keys.LASTNAME);
                            setText(R.id.profile_lastname_text,lastnameString);
                            UserController.getInstance().update(context,Keys.LASTNAME,lastnameString);
                            showLastname(false);
                            removeInputError(R.id.profile_lastname_input_layout);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printError(R.id.profile_lastname_input_layout,"e0");
                        }
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                        printError(R.id.profile_lastname_input_layout,errorCode);
                    }
                    hideProgressBar(R.id.profile_lastname_input_container,R.id.progress_bar_lastname);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveLastname(lastname);
                        }
                    });
                    hideProgressBar(R.id.profile_lastname_input_container,R.id.progress_bar_lastname);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }
    }

    private void savePassword(final String password) {
        showProgressBar(R.id.profile_password_input_container,R.id.progress_bar_password);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.PASSWORD, password, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        removeInputError(R.id.profile_password_input_layout);
                        removeInputError(R.id.profile_password_confirm_input_layout);
                        showPassword(false);
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                        printError(R.id.profile_password_input_layout,errorCode);
                        printError(R.id.profile_password_confirm_input_layout,errorCode);
                    }
                    hideProgressBar(R.id.profile_password_input_container,R.id.progress_bar_password);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savePassword(password);
                        }
                    });
                    hideProgressBar(R.id.profile_password_input_container,R.id.progress_bar_password);
                }
            });
        }catch (UserNotFoundException e){
            SessionController.getInstance().logoutUser(this);
        }

    }

    private void saveUsername(final String username) {
        showProgressBar(R.id.profile_username_input_container,R.id.progress_bar_username);
        try{
            UserController.getInstance().saveUserDetail(this, Keys.USERNAME, username, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if(ConnectionUtils.Success(jsonObject)){
                        try {
                            removeInputError(R.id.profile_username_input_layout);
                            String usernameString = ConnectionUtils.extractJSONValue(jsonObject).getString(Keys.USERNAME);
                            setText(R.id.profile_username_text,usernameString);
                            Preferences.getInstance(context)
                                    .putString(Keys.USERNAME,usernameString)
                                    .commit();
                            showUsername(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            printError(R.id.profile_username_input_layout,"e0");
                        }
                    }else {
                        //If some Backend Error hide Progressbar and handle Error
                        String errorCode = ConnectionUtils.extractErrorCode(jsonObject);
                        printError(R.id.profile_username_input_layout,errorCode);
                    }
                    hideProgressBar(R.id.profile_username_input_container,R.id.progress_bar_username);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    printError(R.id.profile_container,"e100", R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveUsername(username);
                        }
                    });
                    hideProgressBar(R.id.profile_username_input_container,R.id.progress_bar_username);
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

    @Override
    public void onBackPressed() {
        if(isSomethingShown()){
            closeAll();
        }else{
            super.onBackPressed();
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
                Log.e("PICTURE", String.valueOf(thePic.getWidth()));
                Log.e("PICTURE", String.valueOf(thePic.getHeight()));
                savePicture(thePic);
            }
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

    private boolean isSomethingShown(){
        return isEmailShown||isFirstnameShown||isLastnameShown||isUsernameShown||isPasswordShown;
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
