package com.omgproduction.dsport_application.activities.main;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.CommentAdapter;
import com.omgproduction.dsport_application.adapters.LikeAdapter;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.EventService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventDetailActivity extends AbstractFragmentActivity implements CommentAdapter.OnLikeClickedListener, LikeAdapter.OnLikeClickedListener{

    private RecyclerView commentsRecycler, likeRecycler;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager commentLayoutManager, likeLayoutManager;
    private LikeAdapter likeAdapter;
    private Event event;
    private boolean createNewCommentShown = false;
    private boolean likesShown = false;
    private boolean commentsShown = false;
    private Bitmap newCommentBitmap;

    private DateConverter dateConverter;

    private EventService eventService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_event_detail);
        setRefresher((SwipeRefreshLayout) findViewById(R.id.event_detail_refresher));

        dateConverter = new DateConverter();
        eventService = new EventService(this);

        setEventValues((Event) getIntent().getSerializableExtra(INTENT_EVENT));
        showComments(true);
        update();
    }

    private void setEventValues(Event event){
        this.event = event;

        setText(R.id.event_detail_title,event.getTitle());
        setText(R.id.event_detail_comment_count,event.getCommentCount());
        setText(R.id.event_detail_like_count,event.getLikeString(this));
        setText(R.id.event_detail_share_count,event.getShareCount());
        setText(R.id.event_detail_username,event.getUsername());
        setText(R.id.event_detail_text,event.getText());
        setText(R.id.event_detail_date, dateConverter.convertString(this,event.getCreated()));

        findViewById(R.id.event_detail_new_comment_button).setOnClickListener(this);
        findViewById(R.id.event_detail_likes_button).setOnClickListener(this);
        findViewById(R.id.event_detail_comments_button).setOnClickListener(this);
        findViewById(R.id.event_detail_create_comment_button).setOnClickListener(this);


        findViewById(R.id.event_detail_create_comment_gallery_button).setOnClickListener(this);
        findViewById(R.id.event_detail_create_comment_camera_button).setOnClickListener(this);

        findViewById(R.id.event_detail_like_count).setOnClickListener(this);
        findViewById(R.id.event_detail_share_count).setOnClickListener(this);

        setPicture(event.getBitmapPicture(this));
        setEventPicture(event.getBitmapEventPicture(this));
    }

    private void setEventPicture(Bitmap eventPicture){
        if(eventPicture!=null){
            findViewById(R.id.event_detail_event_picture).setVisibility(View.VISIBLE);
            setPic(R.id.event_detail_event_picture,eventPicture);
        }else{
            findViewById(R.id.event_detail_event_picture).setVisibility(View.GONE);
        }
    }

    private void setPicture(Bitmap picture){
        if(picture!=null){
            setPic(R.id.event_detail_picture,picture);
        }
    }

    private void update() {

        User user = getLocalUser();

        eventService.getEventDetail(user.getId(), event.getEvent_id(), new RequestFuture<Event>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Event result) {
                event = result;
                setEventValues(event);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.event_detail_relative_layout, errorCode);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });

        loadComments();

    }

    private void loadComments(){
        eventService.getAllComments(event.getEvent_id(), new RequestFuture<List<Comment>>(){

            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(List<Comment> comments) {
                commentsRecycler = (RecyclerView) findViewById(R.id.event_detail_comment_recycler);
                commentLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
                commentsRecycler.setNestedScrollingEnabled(false);
                commentsRecycler.setLayoutManager(commentLayoutManager);
                commentAdapter = new CommentAdapter(comments);
                commentAdapter.addOnLikeClickedListener(EventDetailActivity.this);
                commentsRecycler.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.event_detail_relative_layout, errorCode);
            }
            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void loadLikes(){
        eventService.getAllLikes(event.getEvent_id(), new RequestFuture<List<Like>>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(List<Like> likes) {
                likeRecycler = (RecyclerView) findViewById(R.id.event_detail_like_recycler);
                likeLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
                likeRecycler.setNestedScrollingEnabled(false);
                likeRecycler.setLayoutManager(likeLayoutManager);
                likeAdapter = new LikeAdapter(likes);
                likeAdapter.addOnLikeClickedListener(EventDetailActivity.this);
                likeRecycler.setAdapter(likeAdapter);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.event_detail_relative_layout, errorCode);
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
            case R.id.event_detail_new_comment_button:
                showNewComment(!createNewCommentShown);
                break;
            case R.id.event_detail_create_comment_button:
                onCreateCommentClick();
                break;
            case R.id.event_detail_like_count:
                onLikeEvent();
                break;
            case R.id.event_detail_likes_button:
                showLikes(!likesShown);
                break;
            case R.id.event_detail_comments_button:
                showComments(!commentsShown);
                break;
            case R.id.event_detail_create_comment_camera_button:
                onCameraButtonPressed();
                break;
            case R.id.event_detail_create_comment_gallery_button:
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

        final String text = ((EditText)findViewById(R.id.event_detail_create_comment_text)).getText().toString();
        String bmp="";
        if(newCommentBitmap!=null){
            bmp = BitmapUtils.getStringFromBitmap(newCommentBitmap);
        }
        final String picture = bmp;

        User user = getLocalUser();

        eventService.createComment(user.getId(), event.getEvent_id(),picture,text, new RequestFuture<Void>(){
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
                printError(R.id.activity_create_event, errorCode, R.string.retry, new View.OnClickListener() {
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
        ((AppCompatEditText)findViewById(R.id.event_detail_create_comment_text)).setText("");
        ((AppCompatImageView)findViewById(R.id.event_detail_create_comment_event_picture)).setImageBitmap(null);
        findViewById(R.id.event_detail_create_comment_event_picture).setVisibility(View.GONE);

        showNewComment(false);
    }

    private void showComments(boolean flag) {
        final View commentsContainer = findViewById(R.id.event_detail_comment_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.event_detail_comments_button);
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
            Animation in_anim = AnimationUtils.loadAnimation(EventDetailActivity.this,R.anim.extrude_y_anim_in);
            commentsContainer.startAnimation(in_anim);
            commentsContainer.setVisibility(View.VISIBLE);
        }
        commentsShown = flag;

    }

    private void showLikes(boolean flag) {
        final View likesContainer = findViewById(R.id.event_detail_like_recycler);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.event_detail_likes_button);
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
            Animation in_anim = AnimationUtils.loadAnimation(EventDetailActivity.this,R.anim.extrude_y_anim_in);
            likesContainer.startAnimation(in_anim);
            likesContainer.setVisibility(View.VISIBLE);
        }
        likesShown = flag;
    }

    private void showNewComment(boolean flag) {
        final View newCommentLayout = findViewById(R.id.event_detail_new_comment_layout);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.event_detail_new_comment_button);
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

    private void onLikeEvent() {

        User user = getLocalUser();

        eventService.likeEvent(user.getId(), event.getEvent_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                event.setLiked(result.isLiked());
                event.setLikeCount(result.getLikeCount());
                //update();
            }

            @Override
            public void onFailure(String errorCode) {printError(R.id.event_detail_relative_layout, errorCode);}

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onLikeComment(final Comment comment, final CommentAdapter.CommentViewHolder holder) {

        User user = getLocalUser();

        eventService.likeComment(user.getId(), comment.getComment_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                comment.setLiked(result.isLiked());
                comment.setLikeCount(result.getLikeCount());
                holder.getTv_likes().setText(comment.getLikeString(EventDetailActivity.this));
            }

            @Override
            public void onFailure(String errorCode) {printError(R.id.event_detail_relative_layout, errorCode);}

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
        findViewById(R.id.event_detail_create_comment_event_picture).setVisibility(View.VISIBLE);
        setPic(R.id.event_detail_create_comment_event_picture,bitmap);
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
