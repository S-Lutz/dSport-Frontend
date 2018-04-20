package com.omgproduction.dsport_application.refactored.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    onSocialItemClickedListener itemClickListener;

    ImageView postProfilePic;
    AppCompatTextView postProfileheaderText;
    AppCompatTextView postProfileheaderSubText;
    ImageView postPicture;
    AppCompatTextView postTitle;
    ImageView postLikebtn;
    ImageView postCommentBtn;
    AppCompatTextView postText;
    AppCompatTextView postlikeCount;
    AppCompatTextView postCommentCount;

    public PostViewHolder(View itemView, onSocialItemClickedListener itemClickListener) {
        super(itemView);

        this.itemClickListener = itemClickListener;

        this.postProfilePic = (ImageView) itemView.findViewById(R.id.post_profile_pic);
        this.postProfileheaderText = (AppCompatTextView) itemView.findViewById(R.id.post_profile_header_text);
        this.postProfileheaderSubText = (AppCompatTextView) itemView.findViewById(R.id.post_profile_header_sub_text);
        this.postPicture = (ImageView) itemView.findViewById(R.id.new_post_picture);
        this.postTitle = (AppCompatTextView) itemView.findViewById(R.id.post_title);
        this.postLikebtn = (ImageView) itemView.findViewById(R.id.post_like_btn);
        this.postCommentBtn = (ImageView) itemView.findViewById(R.id.post_comment_button);
        this.postText = (AppCompatTextView) itemView.findViewById(R.id.new_post_text);
        this.postlikeCount = (AppCompatTextView) itemView.findViewById(R.id.post_like_count);
        this.postCommentCount = (AppCompatTextView) itemView.findViewById(R.id.post_comment_count);

        postCommentBtn.setOnClickListener(this);
        postLikebtn.setOnClickListener(this);
    }


    public ImageView getPostProfilePic() {
        return postProfilePic;
    }

    public void setPostProfilePic(ImageView postProfilePic) {
        this.postProfilePic = postProfilePic;
    }

    public AppCompatTextView getPostProfileheaderText() {
        return postProfileheaderText;
    }

    public void setPostProfileheaderText(AppCompatTextView postProfileheaderText) {
        this.postProfileheaderText = postProfileheaderText;
    }

    public AppCompatTextView getPostProfileheaderSubText() {
        return postProfileheaderSubText;
    }

    public void setPostProfileheaderSubText(AppCompatTextView postProfileheaderSubText) {
        this.postProfileheaderSubText = postProfileheaderSubText;
    }

    public ImageView getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(ImageView postPicture) {
        this.postPicture = postPicture;
    }

    public AppCompatTextView getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(AppCompatTextView postTitle) {
        this.postTitle = postTitle;
    }

    public ImageView getPostLikebtn() {
        return postLikebtn;
    }

    public void setPostLikebtn(ImageView postLikebtn) {
        this.postLikebtn = postLikebtn;
    }

    public ImageView getPostCommentBtn() {
        return postCommentBtn;
    }

    public void setPostCommentBtn(ImageView postCommentBtn) {
        this.postCommentBtn = postCommentBtn;
    }

    public AppCompatTextView getPostText() {
        return postText;
    }

    public void setPostText(AppCompatTextView postText) {
        this.postText = postText;
    }

    public AppCompatTextView getPostlikeCount() {
        return postlikeCount;
    }

    public void setPostlikeCount(AppCompatTextView postlikeCount) {
        this.postlikeCount = postlikeCount;
    }

    public AppCompatTextView getPostCommentCount() {
        return postCommentCount;
    }

    public void setPostCommentCount(AppCompatTextView postCommentCount) {
        this.postCommentCount = postCommentCount;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}