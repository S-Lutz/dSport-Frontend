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
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.CameraOptions;
import com.omgproduction.dsport_application.config.LocalErrorCodes;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.StringUtils;
import com.omgproduction.dsport_application.utils.Transitions;

import java.io.File;
import java.io.IOException;


public class ProfileActivity extends AbstractNavigationActivity implements CameraOptions{

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

        return R.layout.layout_activity_profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ((AppCompatImageView) findViewById(R.id.profile_pic)).setMaxHeight(size.x/2);

        getSupportActionBar().setTitle("");



        preferFloatingButtons();
        addActionListeners();

        update();
    }

    private void update() {
        User user = getLocalUser();

        setUserValues(user);
        userService.synchronizeLocalUser(user.getId(), new RequestFuture<User>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                setUserValues(user);
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                //TODO Errorhandling
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    public void setUserValues(User user){
        setText(R.id.profile_username_text,user.getUsername());
        setText(R.id.profile_email_text,user.getEmail());
        setText(R.id.profile_firstname_text,user.getFirstname());
        setText(R.id.profile_lastname_text,user.getLastname());
        setPic(R.id.profile_pic,BitmapUtils.getBitmapFromString(user.getPicture()));
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
            printInputError(R.id.profile_password_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
        }
        if(password2.trim().isEmpty()){
            printInputError(R.id.profile_password_confirm_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
        }

        if(password1.equals(password2)){
            removeInputError(R.id.profile_password_input_layout);
            removeInputError(R.id.profile_password_confirm_input_layout);
            savePassword(password1);
        }else{
            printInputError(R.id.profile_password_input_layout, LocalErrorCodes.PASSWORD_MISSMATCH_ERROR);
            printInputError(R.id.profile_password_confirm_input_layout, LocalErrorCodes.PASSWORD_MISSMATCH_ERROR);
        }
    }

    private void performEmailConfirm() {
        User user = getLocalUser();

        final String email = getTVText(R.id.profile_email_input);
        if(!email.trim().isEmpty()){
            if(!email.equals(user.getEmail())){
                if(StringUtils.isValidEmail(email)){
                    removeInputError(R.id.profile_email_input_layout);
                    saveEmail(email);
                }else{
                    printInputError(R.id.profile_email_input_layout, LocalErrorCodes.INVALID_EMAIL_ERROR);
                }
            }else{
                printInputError(R.id.profile_email_input_layout, LocalErrorCodes.NO_CANGES_ERROR);
            }
        }else{
            printInputError(R.id.profile_email_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
        }
    }

    private void performLastnameConfirm() {

        User user = getLocalUser();

        String lastname = getTVText(R.id.profile_lastname_input);
        if(!lastname.trim().isEmpty()){
            if(!lastname.equals(user.getLastname())){
                saveLastname(lastname);
                removeInputError(R.id.profile_lastname_input_layout);
            }else{
                printInputError(R.id.profile_lastname_input_layout, LocalErrorCodes.NO_CANGES_ERROR);
            }
        }else{
            printInputError(R.id.profile_lastname_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
        }
    }

    private void performFirstnameConfirm() {

        User user = getLocalUser();

        final String firstname = getTVText(R.id.profile_firstname_input);
        if(!firstname.trim().isEmpty()){
            if(!firstname.equals(user.getFirstname())){
                saveFirstname(firstname);
                removeInputError(R.id.profile_firstname_input_layout);
            }else{
                printInputError(R.id.profile_firstname_input_layout, LocalErrorCodes.NO_CANGES_ERROR);
            }
        }else{
            printInputError(R.id.profile_firstname_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
        }
    }

    private void performUsernameConfirm() {
        User user = getLocalUser();

        final String username = getTVText(R.id.profile_username_input);
        if(!username.trim().isEmpty()){
            if(!username.equals(user.getUsername())){
                saveUsername(username);
                removeInputError(R.id.profile_username_input_layout);
            }else{
                printInputError(R.id.profile_username_input_layout, LocalErrorCodes.NO_CANGES_ERROR);
            }
        }else{
            printInputError(R.id.profile_username_input_layout, LocalErrorCodes.FIELD_EMPTY_ERROR);
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

        User user = getLocalUser();

        String bitmapString = BitmapUtils.getStringFromBitmap(thePic);

        userService.saveUser(new User(Long.valueOf(user.getId()),user.getUsername(), user.getEmail(), bitmapString, user.getFirstname(), user.getLastname(), user.getCreated(), user.getUpdated(), user.getAgbversion()), new RequestFuture<User>(){

            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                setPic(R.id.profile_pic,BitmapUtils.getBitmapFromString(user.getPicture()));
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savePicture(thePic);
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void saveEmail(final String email) {
        User user = getLocalUser();

        //userService.saveUser(new User(user.getId(),user.getUsername(), email, user.getPicture(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getAgbversion()), new RequestFuture<User>(){
        userService.saveUser(new User(Long.valueOf(user.getId()),user.getUsername(), email, user.getPicture(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getUpdated(), user.getAgbversion()), new RequestFuture<User>(){
            @Override
            public void onStartQuery() {
                removeInputError(R.id.profile_email_input_layout);
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                setText(R.id.profile_email_text,user.getEmail());
                setText(R.id.profile_email_input,"");
                showEmail(false);
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveEmail(email);
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void saveFirstname(final String firstname) {

       // User user = getLocalUser();
//
       // userService.saveUser(new User(user.getId(),user.getUsername(), user.getEmail(), user.getPicture(), firstname, user.getLastname(), user.getCreated(), user.getAgbversion()), new RequestFuture<User>(){
       //     @Override
       //     public void onStartQuery() {
       //         removeInputError(R.id.profile_firstname_input_layout);
       //         showProgressBar(true);
       //     }
//
       //     @Override
       //     public void onSuccess(User user) {
       //         setText(R.id.profile_firstname_text,user.getFirstname());
       //         setText(R.id.profile_firstname_input,"");
       //         showFirstname(false);
       //     }
//
       //     @Override
       //     public void onFailure(int errorCode,String errorMessage) {
       //         printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
       //             @Override
       //             public void onClick(View view) {
       //                 saveFirstname(firstname);
       //             }
       //         });
       //     }
//
       //     @Override
       //     public void onFinishQuery() {
       //         showProgressBar(false);
       //     }
       // });
    }

    private void saveLastname(final String lastname) {
        User user = getLocalUser();

       //userService.saveUser(new User(user.getId(),user.getUsername(), user.getEmail(), user.getPicture(), user.getFirstname(), lastname, user.getCreated(), user.getAgbversion()), new RequestFuture<User>(){
       //    @Override
       //    public void onStartQuery() {
       //        removeInputError(R.id.profile_lastname_input_layout);
       //        showProgressBar(true);
       //    }

       //    @Override
       //    public void onSuccess(User user) {
       //        setText(R.id.profile_lastname_text,user.getLastname());
       //        setText(R.id.profile_lastname_input,"");
       //        showLastname(false);
       //    }

       //    @Override
       //    public void onFailure(int errorCode,String errorMessage) {
       //        printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
       //            @Override
       //            public void onClick(View view) {
       //                saveLastname(lastname);
       //            }
       //        });
       //    }

       //    @Override
       //    public void onFinishQuery() {
       //        showProgressBar(false);
       //    }
       //});
    }

    private void savePassword(final String password) {
       // User user = getLocalUser();
//
       // user = new User(user.getId(),user.getUsername(), user.getEmail(), user.getPicture(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getAgbversion());
       // user.setPassword(password);
       // userService.saveUser(user, new RequestFuture<User>(){
       //     @Override
       //     public void onStartQuery() {
       //         showProgressBar(true);
       //         removeInputError(R.id.profile_password_input_layout);
       //         removeInputError(R.id.profile_password_confirm_input_layout);
       //     }
//
       //     @Override
       //     public void onSuccess(User user) {
       //         showPassword(false);
       //         setText(R.id.profile_password_input1,"");
       //         setText(R.id.profile_password_input2,"");
       //     }
//
       //     @Override
       //     public void onFailure(int errorCode,String errorMessage) {
       //         printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
       //             @Override
       //             public void onClick(View view) {
       //                 savePassword(password);
       //             }
       //         });
       //     }
//
       //     @Override
       //     public void onFinishQuery() {
       //         showProgressBar(false);
       //     }
       // });

    }

    private void saveUsername(final String username) {
       // User user = getLocalUser();
//
       // userService.saveUser(new User(user.getId(),username, user.getEmail(), user.getPicture(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getAgbversion()), new RequestFuture<User>(){
       //     @Override
       //     public void onStartQuery() {
       //         removeInputError(R.id.profile_username_input_layout);
       //         showProgressBar(true);
       //     }
//
       //     @Override
       //     public void onSuccess(User user) {
       //         setText(R.id.profile_username_text,user.getUsername());
       //         setText(R.id.profile_username_input,"");
       //         showUsername(false);
       //     }
//
       //     @Override
       //     public void onFailure(int errorCode,String errorMessage) {
       //         printError(R.id.profile_container, errorMessage, R.string.retry, new View.OnClickListener() {
       //             @Override
       //             public void onClick(View view) {
       //                 saveUsername(username);
       //             }
       //         });
       //     }
//
       //     @Override
       //     public void onFinishQuery() {
       //         showProgressBar(false);
       //     }
       // });

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
        update();
    }

    public void onCameraResult(Bitmap bitmap, File file) {
        savePicture(bitmap);
    }

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
