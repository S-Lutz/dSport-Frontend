package com.omgproduction.dsport_application.refactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.helper.PictureUtil;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.PostNode;
import com.omgproduction.dsport_application.refactored.services.PostService;
import com.omgproduction.dsport_application.refactored.views.CheckedEditText;
import com.omgproduction.dsport_application.refactored.views.LoadingView;

import java.io.File;
import java.util.Map;

import static com.omgproduction.dsport_application.refactored.config.CameraOptions.SELECT_PICTURE;

public class CreatePostActivity extends AbstractActivity implements View.OnClickListener {
    private PostService postService;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean cameraRequest = false;
    private Long currentUserId;
    private LoadingView loadingView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.create_post_button).setOnClickListener(this);
        findViewById(R.id.create_post_camera_button).setOnClickListener(this);
        findViewById(R.id.create_post_gallery_button).setOnClickListener(this);

        postService = new PostService();
        currentUserId = getIntent().getExtras().getLong("USER_ID");

        loadingView = (LoadingView) findViewById(R.id.loading_view);
        setUpToolbarTitle();
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new post");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_post_button:
                loadingView.show();
                savePost(cameraRequest);
                break;
            case R.id.create_post_camera_button:
                openCamera();
                break;
            case R.id.create_post_gallery_button:
                openGallery();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            cameraRequest = true;
            setPic(this, findViewById(R.id.imageView1));
        } else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            cameraRequest = false;
            mCurrentPhotoPath = PictureUtil.getPath(this, data.getData());
            setPic(this, findViewById(R.id.imageView1));
        }
    }


    private void savePost(final Boolean camera) {
        CheckedEditText createPostTitle = (CheckedEditText) findViewById(R.id.create_post_title);
        if (!createPostTitle.checkRequired()) return;

        CheckedEditText createPostText = (CheckedEditText) findViewById(R.id.create_post_text);
        if (!createPostText.checkRequired()) return;

        PostNode postNode = new PostNode(createPostTitle.getTextString(),createPostText.getTextString());

        postService.createPost(this, currentUserId, postNode, new BackendCallback<PostNode>() {
            @Override
            public void onSuccess(PostNode result, Map<String, String> responseHeader) {
                savePicture(result, camera);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                Toast.makeText(CreatePostActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void savePicture(PostNode postNode, final Boolean cameraRequest) {
        if (!mCurrentPhotoPath.isEmpty()) {
            final File imgFile = new File(mCurrentPhotoPath);
            postService.uploadPicture(this, imgFile, postNode, new BackendCallback<PostNode>() {
                @Override
                public void onSuccess(PostNode result, Map<String, String> responseHeader) {
                    deleteStoredPic(cameraRequest);
                }

                @Override
                public void onFailure(ErrorResponse error) {
                    deleteStoredPic(cameraRequest);
                }
            });
        }

        loadingView.hide();
        finish();
    }


    @Override
    public int getLayout() {
        return R.layout.new_layout_activity_create_post;
    }

}