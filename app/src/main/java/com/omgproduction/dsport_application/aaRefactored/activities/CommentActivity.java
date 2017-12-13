package com.omgproduction.dsport_application.aaRefactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.CommentAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.CommentNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.SocialResultPair;
import com.omgproduction.dsport_application.aaRefactored.services.CommentService;
import com.omgproduction.dsport_application.aaRefactored.services.LikeService;
import com.omgproduction.dsport_application.aaRefactored.views.CheckedEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CommentActivity extends AbstractActivity implements View.OnClickListener, onSocialItemClickedListener {

    private Boolean getComments = false, getLikes = false;

    private TextView commentViewHeader;
    private ImageView commentPicbtn;
    private ImageView commentSendBtn;

    private CommentService commentService;
    private LikeService likeService;

    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private SocialNode currentSocialNode;

    private ArrayList<SocialResultPair> socialNodes;
    private ArrayList<UserNode> likeUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commentViewHeader = (TextView) findViewById(R.id.comment_header_new);
        commentPicbtn = (ImageView) findViewById(R.id.comments_camera_btn);
        commentSendBtn = (ImageView) findViewById(R.id.comments_sent_btn);

        commentPicbtn.setOnClickListener(this);
        commentSendBtn.setOnClickListener(this);

        socialNodes = new ArrayList<>();

        this.commentService = new CommentService();
        this.likeService = new LikeService();

        currentSocialNode = (SocialNode) getIntent().getSerializableExtra("SOCIAL_NODE");

        setupRecyclerView();

        getComments(currentSocialNode.getId());
        getLike(currentSocialNode.getId());

    }

    @Override
    public int getLayout() {
        return R.layout.new_comment_layout;
    }

    private void setupRecyclerView() {
        commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getComments(Long id) {
        if (!socialNodes.isEmpty()) socialNodes.clear();
        commentService.getComments(this, id, new BackendCallback<ArrayList<SocialResultPair>>() {

            @Override
            public void onSuccess(ArrayList<SocialResultPair> result, Map<String, String> responseHeader) {
                socialNodes = result;
                getComments = true;
                setCommentAdapter();
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });

    }

    private void getLike(Long id) {
        likeService.getLikes(this, id, new BackendCallback<ArrayList<UserNode>>() {
            @Override
            public void onSuccess(ArrayList<UserNode> result, Map<String, String> responseHeader) {
                likeUser = result;
                getLikes = true;
                setCommentAdapter();
                setCommentViewHeader();
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });

    }

    private void setCommentViewHeader() {
        //TODO
        if (likeUser.size() != 0) {
            commentViewHeader.setText(String.valueOf(likeUser.size() + " " + "Likes"));
        } else commentViewHeader.setText("0 Likes");

    }

    public void setCommentAdapter() {
        if (getComments && getLikes) {
            commentAdapter = new CommentAdapter(this, socialNodes, this);
            commentRecyclerView.setAdapter(commentAdapter);
        }
    }

    @Override
    public void onClick(View v, int adapterPosition) {
        SocialResultPair node = socialNodes.get(adapterPosition);

        switch (v.getId()) {
            case R.id.comment_like_btn:
                likeSocialNode(node.getSocialNode());
                adjustIcon(node, adapterPosition);
                break;
        }
    }

    private void adjustIcon(SocialResultPair socialNode, int position) {
        if (socialNode.isLikesSocialNode()) {
            socialNode.setLikesSocialNode(false);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) - 1));
        } else {
            socialNode.setLikesSocialNode(true);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) + 1));
        }
        commentAdapter.notifyItemChanged(position);
    }

    private void likeSocialNode(SocialNode socialNode) {
        likeService.likeSocialNode(this, socialNode.getId(), new BackendCallback<SocialNode>() {
            @Override
            public void onSuccess(SocialNode result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comments_camera_btn:
                openCamera();
                break;
            case R.id.comments_sent_btn:
                createComment();
                break;
        }
    }

    private void createComment() {

        CheckedEditText etCommentText = (CheckedEditText) findViewById(R.id.comments_edit_text);
        if (!etCommentText.checkRequired()) return;

        CommentNode commentNode = new CommentNode();

        commentNode.setText(etCommentText.getTextString());


        if (!Objects.equals(mCurrentPhotoPath, "")) {
            commentNode.setPicture(mCurrentPhotoPath);
        }
        commentService.createComment(this, currentSocialNode.getId(), commentNode, new BackendCallback<CommentNode>() {
            @Override
            public void onSuccess(CommentNode result, Map<String, String> responseHeader) {
                savePicture(result, true);
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }

    private void savePicture(CommentNode commentNode, final Boolean cameraRequest) {
        if (!mCurrentPhotoPath.isEmpty()) {
            final File imgFile = new File(mCurrentPhotoPath);
            commentService.uploadPicture(this, imgFile, commentNode, new BackendCallback<CommentNode>() {
                @Override
                public void onSuccess(CommentNode result, Map<String, String> responseHeader) {
                    deleteStoredPic(cameraRequest);
                }

                @Override
                public void onFailure(ErrorResponse error) {
                    deleteStoredPic(cameraRequest);
                }
            });
        }
        clearView();

    }

    private void clearView() {
        ImageView imageView = (ImageView) findViewById(R.id.new_comment_image_view);
        imageView.setImageDrawable(null);
        imageView.setVisibility(View.GONE);

        CheckedEditText etCommentText = (CheckedEditText) findViewById(R.id.comments_edit_text);
        etCommentText.setText(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic(this, findViewById(R.id.new_comment_image_view));
        }
    }
}