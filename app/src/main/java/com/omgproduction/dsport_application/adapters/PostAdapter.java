package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.Post;

import java.util.ArrayList;

/**
 * Created by Florian on 17.11.2016.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private ArrayList<Post> posts = new ArrayList<>();

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        PostViewHolder viewHolder = new PostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tv_date.setText(post.getCreated());
        holder.tv_shares.setText(post.getShareCount());
        holder.tv_comments.setText(post.getCommentCount());
        holder.tv_likes.setText(post.getLikeCount());
        holder.tv_text.setText(post.getText());
        holder.tv_username.setText(post.getUsername());
        holder.tv_title.setText(post.getTitle());

        Bitmap postPicture = post.getBitmapPostPicture(holder.ccntext);
        Bitmap picture = post.getBitmapPicture(holder.ccntext);
        if(picture!=null){
            holder.iv_picture.setImageBitmap(picture);
        }
        if(postPicture!=null){
            holder.iv_post_picture.setVisibility(View.VISIBLE);
            holder.iv_post_picture.setImageBitmap(postPicture);
        }else{
            holder.iv_post_picture.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_picture, iv_post_picture;
        private TextView tv_username, tv_text, tv_likes, tv_comments, tv_shares, tv_date, tv_title;
        private final Context ccntext;

        public PostViewHolder(View view) {
            super(view);
            ccntext = view.getContext();
            iv_picture = (ImageView) view.findViewById(R.id.post_picture);
            iv_post_picture = (ImageView) view.findViewById(R.id.post_post_picture);
            tv_username = (TextView) view.findViewById(R.id.post_username);
            tv_text = (TextView) view.findViewById(R.id.post_text);
            tv_likes = (TextView) view.findViewById(R.id.post_like_count);
            tv_comments = (TextView) view.findViewById(R.id.post_comment_count);
            tv_shares = (TextView) view.findViewById(R.id.post_share_count);
            tv_date = (TextView) view.findViewById(R.id.post_date);
            tv_title = (TextView) view.findViewById(R.id.post_title);
        }
    }
}
