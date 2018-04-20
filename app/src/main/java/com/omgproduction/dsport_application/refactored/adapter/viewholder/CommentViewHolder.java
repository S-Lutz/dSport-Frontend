package com.omgproduction.dsport_application.refactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    onSocialItemClickedListener itemClickListener;

    private ImageView commentProfilePic;
    private TextView commentOwner;
    private TextView commentCreated;
    private ImageView commentPic;
    private TextView commentText;
    private ImageView commentLikeBtn;
    private TextView commentLikeCount;


    public CommentViewHolder(View itemView, onSocialItemClickedListener itemClickListener) {
        super(itemView);

        this.itemClickListener = itemClickListener;

        commentProfilePic = (ImageView) itemView.findViewById(R.id.comment_item_profile_pic);
        commentCreated = (TextView) itemView.findViewById(R.id.comment_created);
        commentOwner = (TextView) itemView.findViewById(R.id.comment_owner);
        commentPic = (ImageView) itemView.findViewById(R.id.comment_image);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
        commentLikeBtn = (ImageView) itemView.findViewById(R.id.comment_like_btn);
        commentLikeCount = (TextView) itemView.findViewById(R.id.comment_like_count);

        commentLikeBtn.setOnClickListener(this);
    }


    public ImageView getCommentProfilePic() {
        return commentProfilePic;
    }

    public void setCommentProfilePic(ImageView commentProfilePic) {
        this.commentProfilePic = commentProfilePic;
    }

    public TextView getCommentCreated() {
        return commentCreated;
    }

    public void setCommentCreated(TextView commentCreated) {
        this.commentCreated = commentCreated;
    }

    public ImageView getCommentPic() {
        return commentPic;
    }

    public void setCommentPic(ImageView commentPic) {
        this.commentPic = commentPic;
    }

    public TextView getCommentText() {
        return commentText;
    }

    public void setCommentText(TextView commentText) {
        this.commentText = commentText;
    }

    public ImageView getCommentLikeBtn() {
        return commentLikeBtn;
    }

    public void setCommentLikeBtn(ImageView commentLikeBtn) {
        this.commentLikeBtn = commentLikeBtn;
    }

    public TextView getCommentLikeCount() {
        return commentLikeCount;
    }

    public void setCommentLikeCount(TextView commentLikeCount) {
        this.commentLikeCount = commentLikeCount;
    }

    public void setCommentOwner(TextView commentOwner) {
        this.commentOwner = commentOwner;
    }

    public TextView getCommentOwner() {
        return commentOwner;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}