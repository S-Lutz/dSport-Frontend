package com.omgproduction.dsport_application.activities.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.CommentAdapter;
import com.omgproduction.dsport_application.adapters.LikeAdapter;
import com.omgproduction.dsport_application.config.CreatePostStartValues;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PostDetailActivity extends AbstractFragmentActivity implements CommentAdapter.OnLikeClickedListener, LikeAdapter.OnLikeClickedListener{

    private RecyclerView commentsRecycler, likeRecycler;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager, likeLayoutManager;
    private LikeAdapter likeAdapter;
    private Post post;
    private boolean createNewCommentShown = false;
    private boolean likesShown = false;
    private boolean commentsShown = false;
    private Bitmap newCommentBitmap;

    private DateConverter dateConverter;

    private PostService postService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_post_detail);
        setRefresher((SwipeRefreshLayout) findViewById(R.id.post_detail_refresher));

        dateConverter = new DateConverter();
        postService = new PostService(this);

        setPostValues((Post) getIntent().getSerializableExtra(INTENT_POST));
        showComments(true);
        update();
    }

    private void setPostValues(Post post){
        this.post = post;

        setText(R.id.post_detail_title,post.getTitle());
        setText(R.id.post_detail_comment_count,post.getCommentCount());
        setText(R.id.post_detail_like_count,post.getLikeString(this));
        setText(R.id.post_detail_share_count,post.getShareCount());
        setText(R.id.post_detail_username,post.getUsername());
        setText(R.id.post_detail_text,post.getText());
        setText(R.id.post_detail_date, dateConverter.convertString(this,post.getCreated()));

        findViewById(R.id.post_detail_new_comment_button).setOnClickListener(this);
        findViewById(R.id.post_detail_likes_button).setOnClickListener(this);
        findViewById(R.id.post_detail_comments_button).setOnClickListener(this);
        findViewById(R.id.post_detail_create_comment_button).setOnClickListener(this);


        findViewById(R.id.post_detail_create_comment_gallery_button).setOnClickListener(this);
        findViewById(R.id.post_detail_create_comment_camera_button).setOnClickListener(this);

        findViewById(R.id.post_detail_like_count).setOnClickListener(this);
        findViewById(R.id.post_detail_share_count).setOnClickListener(this);

        setPicture(post.getBitmapPicture());
        setPostPicture(post.getBitmapPostPicture());
    }

    private void setPostPicture(Bitmap postPicture){
        if(postPicture!=null){
            findViewById(R.id.post_detail_post_picture).setVisibility(View.VISIBLE);
            setPic(R.id.post_detail_post_picture,postPicture);
        }else{
            findViewById(R.id.post_detail_post_picture).setVisibility(View.GONE);
        }
    }

    private void setPicture(Bitmap picture){
        if(picture!=null){
            setPic(R.id.post_detail_picture,picture);
        }
    }

    private void update() {

        User user = getLocalUser();

        postService.getPostDetail(user.getId(), post.getPost_id(), new RequestFuture<Post>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Post result) {
                post = result;
                setPostValues(post);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.post_detail_relative_layout, errorCode);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });

        loadComments();

    }

    private void loadComments(){
        postService.getAllComments(post.getPost_id(), new RequestFuture<List<Comment>>(){

            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(List<Comment> comments) {
                commentsRecycler = (RecyclerView) findViewById(R.id.post_detail_comment_recycler);
                commentLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
                commentsRecycler.setNestedScrollingEnabled(false);
                commentsRecycler.setLayoutManager(commentLayoutManager);
                commentAdapter = new CommentAdapter(comments);
                commentAdapter.addOnLikeClickedListener(PostDetailActivity.this);
                commentsRecycler.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.post_detail_relative_layout, errorCode);
            }
            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void loadLikes(){
        postService.getAllLikes(post.getPost_id(), new RequestFuture<List<Like>>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(List<Like> likes) {
                likeRecycler = (RecyclerView) findViewById(R.id.post_detail_like_recycler);
                likeLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
                likeRecycler.setNestedScrollingEnabled(false);
                likeRecycler.setLayoutManager(likeLayoutManager);
                likeAdapter = new LikeAdapter(likes);
                likeAdapter.addOnLikeClickedListener(PostDetailActivity.this);
                likeRecycler.setAdapter(likeAdapter);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.post_detail_relative_layout, errorCode);
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
        update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_detail_new_comment_button:
                showNewComment(!createNewCommentShown);
                break;
            case R.id.post_detail_create_comment_button:
                onCreateCommentClick();
                break;
            case R.id.post_detail_like_count:
                onLikePost();
                break;
            case R.id.post_detail_likes_button:
                showLikes(!likesShown);
                break;
            case R.id.post_detail_comments_button:
                showComments(!commentsShown);
                break;
            case R.id.post_detail_create_comment_camera_button:
                onCameraButtonPressed();
                break;
            case R.id.post_detail_create_comment_gallery_button:
                onGalleryButtonPressed();
                break;
        }
    }

    private void onGalleryButtonPressed() {
        openGallery();
    }

    private void onCameraButtonPressed() {
        openCamera();
    }

    private void onCreateCommentClick() {

        final String text = ((EditText)findViewById(R.id.post_detail_create_comment_text)).getText().toString();
        String bmp="";
        if(newCommentBitmap!=null){
            bmp = BitmapUtils.getStringFromBitmap(newCommentBitmap);
        }
        final String picture = bmp;

        User user = getLocalUser();

        postService.createComment(user.getId(), post.getPost_id(),picture,text, new RequestFuture<Void>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Void result) {
                update();
                clearNewComment();
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.activity_create_post, errorCode, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCreateCommentClick();
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void clearNewComment() {
        ((AppCompatEditText)findViewById(R.id.post_detail_create_comment_text)).setText("");
        ((AppCompatImageView)findViewById(R.id.post_detail_create_comment_post_picture)).setImageBitmap(null);
        findViewById(R.id.post_detail_create_comment_post_picture).setVisibility(View.GONE);

        showNewComment(false);
    }

    private void showComments(boolean flag) {
        final View commentsContainer = findViewById(R.id.post_detail_comment_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_comments_button);
        if(!flag){
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_arrow, 0);
            Animation out_anim = AnimationUtils.loadAnimation(this,R.anim.extrude_y_anim_out);
            out_anim.setAnimationListener(new AnimationAdapter(){
                @Override
                public void onAnimationEnd(Animation animation) {
                    commentsContainer.setVisibility(View.GONE);
                }
            });
            commentsContainer.startAnimation(out_anim);
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_expand, 0);
            Animation in_anim = AnimationUtils.loadAnimation(PostDetailActivity.this,R.anim.extrude_y_anim_in);
            commentsContainer.startAnimation(in_anim);
            commentsContainer.setVisibility(View.VISIBLE);
        }
        commentsShown = flag;

    }

    private void showLikes(boolean flag) {
        final View likesContainer = findViewById(R.id.post_detail_like_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_likes_button);
        if(!flag){
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_arrow, 0);
            Animation out_anim = AnimationUtils.loadAnimation(this,R.anim.extrude_y_anim_out);
            out_anim.setAnimationListener(new AnimationAdapter(){
                @Override
                public void onAnimationEnd(Animation animation) {
                    likesContainer.setVisibility(View.GONE);
                }
            });
            likesContainer.startAnimation(out_anim);
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_expand, 0);
            loadLikes();
            Animation in_anim = AnimationUtils.loadAnimation(PostDetailActivity.this,R.anim.extrude_y_anim_in);
            likesContainer.startAnimation(in_anim);
            likesContainer.setVisibility(View.VISIBLE);
        }
        likesShown = flag;
    }

    private void showNewComment(boolean flag) {
        final View newCommentLayout = findViewById(R.id.post_detail_new_comment_layout);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_new_comment_button);
        if(!flag){
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_arrow, 0);
            Animation out_anim = AnimationUtils.loadAnimation(this,R.anim.extrude_y_anim_out);
            out_anim.setAnimationListener(new AnimationAdapter(){
                @Override
                public void onAnimationEnd(Animation animation) {
                    newCommentLayout.setVisibility(View.GONE);
                }
            });
            newCommentLayout.startAnimation(out_anim);
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(  0, 0,R.drawable.ic_expand, 0);
            Animation in_anim = AnimationUtils.loadAnimation(this,R.anim.extrude_y_anim_in);
            newCommentLayout.startAnimation(in_anim);
            newCommentLayout.setVisibility(View.VISIBLE);
        }
        createNewCommentShown = flag;
    }

    private void onLikePost() {

        User user = getLocalUser();

        postService.likePost(user.getId(), post.getPost_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                post.setLiked(result.isLiked());
                post.setLikeCount(result.getLikeCount());
                //update();
            }

            @Override
            public void onFailure(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onLikeComment(final Comment comment, final CommentAdapter.CommentViewHolder holder) {

        User user = getLocalUser();

        postService.likeComment(user.getId(), comment.getComment_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                comment.setLiked(result.isLiked());
                comment.setLikeCount(result.getLikeCount());
                holder.getTv_likes().setText(comment.getLikeString(PostDetailActivity.this));
            }

            @Override
            public void onFailure(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void onLikeSelected(Like like) {
        //TODO OPEN USER WHO LIKED
    }

    public void onCameraResult(Bitmap bitmap, File file) {
        showNewComment(true);
        findViewById(R.id.post_detail_create_comment_post_picture).setVisibility(View.VISIBLE);
        setPic(R.id.post_detail_create_comment_post_picture,bitmap);
        newCommentBitmap = bitmap;
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
            cropIntent.putExtra("outputX", 2048);
            cropIntent.putExtra("outputY", 1024);
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
