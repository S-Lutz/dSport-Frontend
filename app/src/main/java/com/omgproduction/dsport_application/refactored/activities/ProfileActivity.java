package com.omgproduction.dsport_application.refactored.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.helper.PictureUtil;
import com.omgproduction.dsport_application.refactored.listeners.AnimationAdapter;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.services.PreferencesService;
import com.omgproduction.dsport_application.refactored.services.UserService;
import com.omgproduction.dsport_application.refactored.views.CheckedEditText;
import com.omgproduction.dsport_application.refactored.views.LoadingView;

import java.io.File;
import java.util.Map;

import static com.omgproduction.dsport_application.refactored.helper.SlideAnimationUtil.slideInFromRight;
import static com.omgproduction.dsport_application.refactored.helper.SlideAnimationUtil.slideOutRight;
import static com.omgproduction.dsport_application.refactored.config.CameraOptions.SELECT_PICTURE;


public class ProfileActivity extends AbstractActivity implements View.OnClickListener {

    private UserService userService;

    public static final String USER_USERNAME = "username", USER_FIRSTNAME = "firstname", USER_LASTNAME = "lastname", USER_EMAIL = "email";

    private FloatingActionButton fabEdit, fabGallery, fabCamera;
    private Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise, fabOpenBig;
    private boolean isEditFabOpen = false;

    private Pair<View, View> current;
    private boolean isUsernameShown = false;
    private boolean isLastnameShown = false;
    private boolean isFirstnameShown = false;
    private boolean isEmailShown = false;
    private boolean isPasswordShown = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private ImageView backgroundImageView;

    private LoadingView loadingView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserService();
        loadingView = (LoadingView) findViewById(R.id.loading_view);
        mImageView = (ImageView) findViewById(R.id.new_profile_pic);
        backgroundImageView = (ImageView) findViewById(R.id.profile_background);
       // backgroundImageView.setColorFilter(R.color.colorPrimary);

        //TODO loadingView is not shown!
        loadingView.show();

        updateView();
        addActionListeners();
        preferFloatingButtons();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            saveImg(mCurrentPhotoPath, true);
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            saveImg(PictureUtil.getPath(this, data.getData()), false);
        }
    }


    private void preferFloatingButtons() {
        fabEdit = (FloatingActionButton) findViewById(R.id.profile_edit_fab);
        fabCamera = (FloatingActionButton) findViewById(R.id.profile_edit_fab_camera);
        fabGallery = (FloatingActionButton) findViewById(R.id.profile_edit_fab_gallery);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabOpenBig = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_big);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anti_clockwise);

        fabEdit.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabGallery.setOnClickListener(this);
    }

    public void setUserValues(UserNode user) {
        setText(R.id.username_toolbar, user.getUsername());
        setText(R.id.new_profile_username_text, user.getUsername());
        setText(R.id.new_profile_email_text, user.getEmail());
        setText(R.id.new_profile_firstname_text, user.getFirstname());
        setText(R.id.new_profile_lastname_text, user.getLastname());
        if (user.getPicture() != null){
            setPic(mImageView, user.getPicture());
        }
    }

    public void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    public void setPic(ImageView view, String url) {
        GlideApp
                .with(this)
                .load(url)
                .fitCenter()
                .into(view);
    }


    private void addActionListeners() {
        //setRefresher((SwipeRefreshLayout)findViewById(R.id.profile_refresher));
        findViewById(R.id.new_profile_firstname_edit).setOnClickListener(this);
        findViewById(R.id.new_profile_lastname_edit).setOnClickListener(this);
        findViewById(R.id.new_profile_username_edit).setOnClickListener(this);
        findViewById(R.id.new_profile_email_edit).setOnClickListener(this);
        findViewById(R.id.new_profile_password_edit).setOnClickListener(this);
        findViewById(R.id.new_profile_firstname_confirm).setOnClickListener(this);
        findViewById(R.id.new_profile_lastname_confirm).setOnClickListener(this);
        findViewById(R.id.new_profile_username_confirm).setOnClickListener(this);
        findViewById(R.id.new_profile_email_confirm).setOnClickListener(this);
        findViewById(R.id.new_profile_password_confirm).setOnClickListener(this);
        findViewById(R.id.new_profile_pic).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (current != null) {
            closeCurrent();
        }
        switch (v.getId()) {
            case R.id.new_profile_username_edit:
                if (!isUsernameShown) {
                    setCurrent(findViewById(R.id.new_profile_username_edit), findViewById(R.id.new_profile_username_input_container));
                } else {
                    clearCurrent(findViewById(R.id.new_profile_username_edit), findViewById(R.id.new_profile_username_input_container));
                }
                isUsernameShown ^= true;
                break;
            case R.id.new_profile_firstname_edit:
                if (!isFirstnameShown) {
                    setCurrent(findViewById(R.id.new_profile_firstname_edit), findViewById(R.id.new_profile_firstname_input_container));
                } else {
                    clearCurrent(findViewById(R.id.new_profile_firstname_edit), findViewById(R.id.new_profile_firstname_input_container));
                }
                isFirstnameShown ^= true;
                break;
            case R.id.new_profile_lastname_edit:
                if (!isLastnameShown) {
                    setCurrent(findViewById(R.id.new_profile_lastname_edit), findViewById(R.id.new_profile_lastname_input_container));
                } else {
                    clearCurrent(findViewById(R.id.new_profile_lastname_edit), findViewById(R.id.new_profile_lastname_input_container));
                }
                isLastnameShown ^= true;
                break;
            case R.id.new_profile_email_edit:
                if (!isEmailShown) {
                    setCurrent(findViewById(R.id.new_profile_email_edit), findViewById(R.id.new_profile_email_input_container));
                } else {
                    clearCurrent(findViewById(R.id.new_profile_email_edit), findViewById(R.id.new_profile_email_input_container));
                }
                isEmailShown ^= true;
                break;
            case R.id.new_profile_password_edit:
                if (!isPasswordShown) {
                    setCurrent(findViewById(R.id.new_profile_password_edit), findViewById(R.id.new_profile_password_input_container));
                } else {
                    clearCurrent(findViewById(R.id.new_profile_password_edit), findViewById(R.id.new_profile_password_input_container));
                }
                isPasswordShown ^= true;
                break;
            case R.id.new_profile_username_confirm:
                performConfirm(R.id.new_profile_username_input, USER_USERNAME);
                break;
            case R.id.new_profile_firstname_confirm:
                performConfirm(R.id.new_profile_firstname_input, USER_FIRSTNAME);
                break;
            case R.id.new_profile_lastname_confirm:
                performConfirm(R.id.new_profile_lastname_input, USER_LASTNAME);
                break;
            case R.id.new_profile_email_confirm:
                performConfirm(R.id.new_profile_email_input, USER_EMAIL);
                break;
            case R.id.new_profile_password_confirm:
                performPasswordConfirm(R.id.new_profile_password_input1, R.id.new_profile_password_input2);
                break;
            case R.id.profile_edit_fab:
                performFabClick();
                break;
            case R.id.profile_edit_fab_camera:
                performFabCameraClick();
                break;
            case R.id.profile_edit_fab_gallery:
                performFabGalleryClick();
                break;
            case R.id.new_profile_pic:
                performPicClick(this);
                break;
        }

    }

    public void setCurrent(View v1, View v2) {
        current = new Pair<>(v1, v2);
        showAnimation(v1.getId(), v2.getId());
        closeOthers();
    }

    public void clearCurrent(View v1, View v2) {
        current = null;
        hideAnimation(v1.getId(), v2.getId());
    }

    public void closeCurrent() {
        hideAnimation(current.first.getId(), current.second.getId());
    }

    public void closeOthers() {
        isUsernameShown = false;
        isLastnameShown = false;
        isFirstnameShown = false;
        isEmailShown = false;
        isPasswordShown = false;
    }

    public void updateView() {
        setUserValues(new Gson().
                fromJson(PreferencesService.getSharedPreferencesUser(this), UserNode.class));
    }

    public void performConfirm(int vId, String tag) {

        final CheckedEditText checkedEditText = (CheckedEditText) findViewById(vId);
        if (!checkedEditText.checkRequired()) return;

        UserNode updatedUser = new Gson().fromJson(PreferencesService.getSharedPreferencesUser(this), UserNode.class);
        switch (tag) {
            case USER_USERNAME:
                updatedUser.setUsername(checkedEditText.getTextString());
                break;
            case USER_FIRSTNAME:
                updatedUser.setFirstname(checkedEditText.getTextString());
                break;
            case USER_LASTNAME:
                updatedUser.setLastname(checkedEditText.getTextString());
                break;
            case USER_EMAIL:
                updatedUser.setEmail(checkedEditText.getTextString());
                break;
        }

        closeCurrent();
        loadingView.show();

        userService.update(this, updatedUser, new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                checkedEditText.setText("");
                updateView();
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                checkedEditText.setText("");
                Toast.makeText(ProfileActivity.this, "Already in use", Toast.LENGTH_SHORT).show();
                loadingView.hide();
            }
        });
    }

    public void performPasswordConfirm(int vId1, int vId2) {

        CheckedEditText password_1ET = (CheckedEditText) findViewById(vId1);
        CheckedEditText password_2ET = (CheckedEditText) findViewById(vId2);
        if (!password_1ET.checkRequired() || !password_2ET.checkRequired() || !password_1ET.checkContentEquals(password_2ET))
            return;

        UserNode updatedUser = new Gson().fromJson(PreferencesService.getSharedPreferencesUser(this), UserNode.class);
        updatedUser.setPassword(password_1ET.getTextString());

        closeCurrent();
        loadingView.show();

        userService.update(this, updatedUser, new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
            }
        });


    }

    private void hideAnimation(final int vMain, final int container, final View... vOthers) {
        fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                slideOutRight((ViewGroup) findViewById(R.id.new_profile_container), findViewById(container));
            }
        });
    }

    private void showAnimation(final int vMain, final int container, View... vOthers) {
        fadeInOut(findViewById(vMain)).setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                slideInFromRight((ViewGroup) findViewById(R.id.new_profile_container), findViewById(container));
            }
        });
    }

    private Animation fadeInOut(View view) {
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
        view.startAnimation(fade);
        return fade;
    }

    private void performFabClick() {
        if (isEditFabOpen) {
            fabCamera.startAnimation(fabClose);
            fabGallery.startAnimation(fabClose);
            fabEdit.setAnimation(fabClockWise);
            fabGallery.setClickable(false);
            fabCamera.setClickable(false);
            isEditFabOpen = false;
        } else {
            fabCamera.startAnimation(fabOpen);
            fabGallery.startAnimation(fabOpen);
            fabEdit.setAnimation(fabAntiClockWise);
            fabGallery.setClickable(true);
            fabCamera.setClickable(true);
            isEditFabOpen = true;
        }
    }

    private void performPicClick(Context context) {
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra("imageUrl", mCurrentPhotoPath);
        context.startActivity(intent);
    }

    private void performFabGalleryClick() {
        performFabClick();
        openGallery();
    }

    private void performFabCameraClick() {
        performFabClick();
        openCamera();
    }

    private void saveImg(String url, final Boolean cameraRequest) {
        final File imgFile = new File(url);
        UserNode updatedUser = new Gson().fromJson(PreferencesService.getSharedPreferencesUser(this), UserNode.class);
        userService.uploadPicture(this, RouteGenerator.generateUploadFileRoute(), imgFile, updatedUser, new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                setPic(mImageView, result.getPicture());
                deleteStoredPic(cameraRequest);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                deleteStoredPic(cameraRequest);
                Toast.makeText(ProfileActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.new_layout_activity_profile;
    }

}

