package com.omgproduction.dsport_application.refactored.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.adapter.viewholder.CommentViewHolder;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.refactored.models.nodes.CommentNode;

import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.models.resultnodes.SocialResultPair;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private onSocialItemClickedListener itemClickListener;

    private Context context;
    private ArrayList<SocialResultPair> socialNodes;


    public CommentAdapter(Context context, ArrayList<SocialResultPair> socialNodes, onSocialItemClickedListener itemClickListener) {
        this.socialNodes = socialNodes;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_custom_row_layout_comment, parent, false);
        return new CommentViewHolder(item, itemClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;

        SocialResultPair currentPair = socialNodes.get(position);
        CommentNode commentNode = (CommentNode) currentPair.getSocialNode();
        UserNode commentOwner = currentPair.getUserNode();

        if (commentOwner.getPicture() != null) {
            GlideApp
                    .with(context)
                    .load(commentOwner.getPicture())
                    .circleCrop()
                    .into(commentViewHolder.getCommentProfilePic());
        }

        commentViewHolder.getCommentOwner().setText(commentOwner.getUsername());
        commentViewHolder.getCommentCreated().setText(commentNode.getCreated());

        if (commentNode.getPicture() != null) {
            commentViewHolder.getCommentPic().setVisibility(View.VISIBLE);
            GlideApp
                    .with(context)
                    .load(commentNode.getPicture())
                    .centerCrop()
                    .into(commentViewHolder.getCommentPic());
        }

        commentViewHolder.getCommentText().setText(commentNode.getText());
        commentViewHolder.getCommentLikeCount().setText(commentNode.getLikeCount());

        if (currentPair.isLikesSocialNode())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                commentViewHolder.getCommentLikeBtn().setColorFilter(new LightingColorFilter(context.getColor(R.color.colorPrimary), context.getColor(R.color.colorPrimary)));
            }else
                commentViewHolder.getCommentLikeBtn().setColorFilter(new LightingColorFilter(Color.RED, Color.RED));

    }

    @Override
    public int getItemCount() {
        return socialNodes.size();
    }
}