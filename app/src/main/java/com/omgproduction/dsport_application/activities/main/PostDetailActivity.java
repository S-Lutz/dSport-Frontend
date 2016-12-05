package com.omgproduction.dsport_application.activities.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.CommentAdapter;
import com.omgproduction.dsport_application.adapters.LikeAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.controller.ApplicationController;
import com.omgproduction.dsport_application.controller.PostController;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AdvancedActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.CameraManager;
import com.omgproduction.dsport_application.utils.DateUtils;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.bitmap;

public class PostDetailActivity extends AdvancedActivity implements OnResultListener<ArrayList<Comment>>, CommentAdapter.OnLikeClickedListener, LikeAdapter.OnLikeClickedListener{

    private RecyclerView commentsRecycler, likeRecycler;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager, likeLayoutManager;
    private LikeAdapter likeAdapter;
    private Post post;
    private boolean createNewCommentShown = false;
    private boolean likesShown = false;
    private boolean commentsShown = false;
    private Bitmap newCommentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        setRefresher((SwipeRefreshLayout) findViewById(R.id.post_detail_refresher));


        setPostValues((Post) getIntent().getSerializableExtra(ApplicationKeys.POSTS));
        showComments(true);
        update();
    }

    private void setPostValues(Post post){
        this.post = post;

        setText(R.id.post_detail_title,post.getTitle());
        setText(R.id.post_detail_comment_count,post.getCommentCount());
        setText(R.id.post_detail_like_count,post.getLikeCount());
        setText(R.id.post_detail_share_count,post.getShareCount());
        setText(R.id.post_detail_username,post.getUsername());
        setText(R.id.post_detail_text,post.getText());
        setText(R.id.post_detail_date, DateUtils.convertString(this,post.getCreated()));

        findViewById(R.id.post_detail_new_comment_button).setOnClickListener(this);
        findViewById(R.id.post_detail_likes_button).setOnClickListener(this);
        findViewById(R.id.post_detail_comments_button).setOnClickListener(this);
        findViewById(R.id.post_detail_create_comment_button).setOnClickListener(this);


        findViewById(R.id.post_detail_create_comment_gallery_button).setOnClickListener(this);
        findViewById(R.id.post_detail_create_comment_camera_button).setOnClickListener(this);

        findViewById(R.id.post_detail_like_count).setOnClickListener(this);
        findViewById(R.id.post_detail_share_count).setOnClickListener(this);

        setPicture(post.getBitmapPicture(this));
        setPostPicture(post.getBitmapPostPicture(this));
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
        PostController.getInstance().getAllComments(PostDetailActivity.this, post.getPost_id(), PostDetailActivity.this);
        PostController.getInstance().getPostDetail(post.getPost_id(), new OnResultAdapter<Post>(){
            @Override
            public void onStartQuery() {
                refresher.setRefreshing(true);
            }

            @Override
            public void onSuccess(Post result) {
                post = result;
                setPostValues(post);
            }
            @Override
            public void onConnectionError(VolleyError e) {
                e.printStackTrace();
                printError(R.id.post_detail_relative_layout, "e100");
            }

            @Override
            public void onBackendError(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

            @Override
            public void onJSONException(JSONException e) {
                e.printStackTrace();
                printError(R.id.post_detail_relative_layout, "e0");
            }

            @Override
            public void onFinishQuery() {
                refresher.setRefreshing(false);
            }
        });
    }

    private void loadLikes(){

        UserController.getInstance().getLocalUserID(this, new OnResultAdapter<String>() {
            @Override
            public void onStartQuery() {
                refresher.setRefreshing(true);
            }

            @Override
            public void onSuccess(String user) {
                PostController.getInstance().getAllLikes(PostDetailActivity.this, post.getPost_id(), new OnResultAdapter<ArrayList<Like>>(){
                    @Override
                    public void onStartQuery() {
                        refresher.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(ArrayList<Like> likes) {
                        likeRecycler = (RecyclerView) findViewById(R.id.post_detail_like_recycler);
                        likeLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
                        likeRecycler.setNestedScrollingEnabled(false);
                        likeRecycler.setLayoutManager(likeLayoutManager);
                        likeAdapter = new LikeAdapter(likes);
                        likeAdapter.addOnLikeClickedListener(PostDetailActivity.this);
                        likeRecycler.setAdapter(likeAdapter);
                    }
                    @Override
                    public void onConnectionError(VolleyError e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e100");
                    }

                    @Override
                    public void onBackendError(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

                    @Override
                    public void onJSONException(JSONException e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e0");
                    }
                    @Override
                    public void onUserNotFound() {SessionController.getInstance().logout(PostDetailActivity.this);}

                    @Override
                    public void onFinishQuery() {
                        refresher.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(PostDetailActivity.this);
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

    @Override
    protected void onBitmapResult(Bitmap bitmap) {
        findViewById(R.id.post_detail_create_comment_post_picture).setVisibility(View.VISIBLE);
        setPic(R.id.post_detail_create_comment_post_picture,bitmap);
        newCommentBitmap = bitmap;
        showNewComment(true);
    }

    private void onCreateCommentClick() {

        final String text = ((EditText)findViewById(R.id.post_detail_create_comment_text)).getText().toString();
        String bmp="";
        if(newCommentBitmap!=null){
            bmp = BitmapUtils.getStringFromBitmap(newCommentBitmap);
        }
        final String picture = bmp;


        UserController.getInstance().getLocalUser(this,new OnResultAdapter<User>(){
            @Override
            public void onStartQuery() {
                refresher.setRefreshing(true);
            }

            @Override
            public void onSuccess(User user) {
                PostController.getInstance().createComment(user.getId(), post.getPost_id(),picture,text, new OnResultAdapter<Void>(){
                    @Override
                    public void onStartQuery() {
                        refresher.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        update();
                        clearNewComment();
                    }

                    @Override
                    public void onConnectionError(VolleyError e) {
                        printError(R.id.activity_create_post, "e100", R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onCreateCommentClick();
                            }
                        });
                    }

                    @Override
                    public void onBackendError(String errorCode) {
                        printError(R.id.activity_create_post, errorCode, R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onCreateCommentClick();
                            }
                        });
                    }

                    @Override
                    public void onUserNotFound() {
                        SessionController.getInstance().logout(PostDetailActivity.this);
                    }

                    @Override
                    public void onFinishQuery() {
                        refresher.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(PostDetailActivity.this);
            }

            @Override
            public void onFinishQuery() {
                refresher.setRefreshing(false);
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
        UserController.getInstance().getLocalUserID(this, new OnResultAdapter<String>(){
            @Override
            public void onSuccess(String result) {
                PostController.getInstance().likePost(result, post.getPost_id(), new OnResultAdapter<Void>(){
                    @Override
                    public void onSuccess(Void result) {
                        //TODO Vermeide mehrfach like
                        update();
                    }
                    @Override
                    public void onConnectionError(VolleyError e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e100");
                    }

                    @Override
                    public void onBackendError(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

                    @Override
                    public void onJSONException(JSONException e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e0");
                    }
                });
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(PostDetailActivity.this);
            }
        });
    }

    @Override
    public void onStartQuery() {
        refresher.setRefreshing(true);
    }

    @Override
    public void onSuccess(ArrayList<Comment> comments) {
        commentsRecycler = (RecyclerView) findViewById(R.id.post_detail_comment_recycler);
        commentLayoutManager = new LinearLayoutManager(this);
        commentsRecycler.setNestedScrollingEnabled(false);
        commentsRecycler.setLayoutManager(commentLayoutManager);
        commentAdapter = new CommentAdapter(comments);
        commentAdapter.addOnLikeClickedListener(this);
        commentsRecycler.setAdapter(commentAdapter);
    }

    @Override
    public void onConnectionError(VolleyError e) {
        e.printStackTrace();
        printError(R.id.post_detail_relative_layout, "e100");
    }

    @Override
    public void onBackendError(String errorCode) {
        printError(R.id.post_detail_relative_layout, errorCode);
    }

    @Override
    public void onJSONException(JSONException e) {
        e.printStackTrace();
        printError(R.id.post_detail_relative_layout, "e0");
    }

    @Override
    public void onUserNotFound() {
        SessionController.getInstance().logout(this);
    }
    @Override
    public void onFinishQuery() {
        refresher.setRefreshing(false);
    }

    @Override
    public void onLikeComment(final Comment comment) {
        UserController.getInstance().getLocalUserID(this, new OnResultAdapter<String>(){
            @Override
            public void onSuccess(String result) {
                PostController.getInstance().likeComment(result, comment.getComment_id(), new OnResultAdapter<Void>(){

                    @Override
                    public void onSuccess(Void result) {
                        //TODO Verhindere mehrfach Like
                        update();
                    }

                    @Override
                    public void onConnectionError(VolleyError e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e100");
                    }

                    @Override
                    public void onBackendError(String errorCode) {printError(R.id.post_detail_relative_layout, errorCode);}

                    @Override
                    public void onJSONException(JSONException e) {
                        e.printStackTrace();
                        printError(R.id.post_detail_relative_layout, "e0");
                    }
                });
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(PostDetailActivity.this);
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
}
