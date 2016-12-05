package com.omgproduction.dsport_application.activities.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.CommentAdapter;
import com.omgproduction.dsport_application.adapters.LikeAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.controller.PostController;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.supplements.activities.AdvancedActivity;

import org.json.JSONException;

import java.util.ArrayList;

public class PostDetailActivity extends AdvancedActivity implements OnResultListener<ArrayList<Comment>>, CommentAdapter.OnLikeClickedListener, LikeAdapter.OnLikeClickedListener{

    private RecyclerView commentsRecycler, likeRecycler;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager, likeLayoutManager;
    private LikeAdapter likeAdapter;
    private SwipeRefreshLayout refresher;
    private Post post;
    private boolean createNewCommentShown = false;
    private boolean likesShown = false;
    private boolean commentsShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        refresher = (SwipeRefreshLayout) findViewById(R.id.post_detail_refresher);
        refresher.setOnRefreshListener(this);

        setPostValues((Post) getIntent().getSerializableExtra(ApplicationKeys.POSTS));
        showComments();
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
        setText(R.id.post_detail_date,post.getCreated());

        findViewById(R.id.post_detail_new_comment_button).setOnClickListener(this);
        findViewById(R.id.post_detail_likes_button).setOnClickListener(this);
        findViewById(R.id.post_detail_comments_button).setOnClickListener(this);

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
        //TODO PostController.getInstance().getPost()
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
                        likeRecycler.setLayoutManager(commentLayoutManager);
                        likeRecycler.setHasFixedSize(true);
                        likeAdapter = new LikeAdapter(likes);
                        likeAdapter.addOnLikeClickedListener(PostDetailActivity.this);
                        likeRecycler.setAdapter(commentAdapter);
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
                onNewCommentClick();
                break;
            case R.id.post_detail_like_count:
                onLikePost();
                break;
            case R.id.post_detail_likes_button:
                showLikes();
                break;
            case R.id.post_detail_comments_button:
                showComments();
                break;
        }
    }

    private void showComments() {
        final View commentsContainer = findViewById(R.id.post_detail_comment_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_comments_button);
        if(commentsShown){
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
        commentsShown = !commentsShown;

    }

    private void showLikes() {
        final View likesContainer = findViewById(R.id.post_detail_like_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_likes_button);
        if(likesShown){
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
        likesShown = !likesShown;
    }

    private void onNewCommentClick() {
        final View newCommentLayout = findViewById(R.id.post_detail_new_comment_layout);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.post_detail_new_comment_button);
        if(createNewCommentShown){
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
        createNewCommentShown = !createNewCommentShown;
    }

    private void onLikePost() {
        UserController.getInstance().getLocalUserID(this, new OnResultAdapter<String>(){
            @Override
            public void onSuccess(String result) {
                PostController.getInstance().likePost(result, post.getPost_id(), new OnResultAdapter<Void>(){
                    @Override
                    public void onSuccess(Void result) {
                        //TODO Vermeide mehrfach like
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
        commentsRecycler.setLayoutManager(commentLayoutManager);
        commentsRecycler.setHasFixedSize(true);
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
