package com.omgproduction.dsport_application.adapters;

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
 * Created by Florian on 01.12.2016.
 */

public class CommentAdapter {

    /*
    private ArrayList<Comment> posts = new ArrayList<>();

    public interface OnPostClickedListener{
        void onPostClicked(Post p);
    }

    private final ArrayList<PostAdapter.OnPostClickedListener> onPostClickedListeners = new ArrayList<>();

    public CommentAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        PostAdapter.PostViewHolder viewHolder = new PostAdapter.PostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tv_date.setText(post.getCreated());
        holder.tv_shares.setText(post.getShareCount());
        holder.tv_comments.setText(post.getCommentCount());
        holder.tv_likes.setText(post.getLikeCount());
        holder.tv_text.setText(post.getText());
        holder.tv_username.setText(post.getUsername());
        holder.tv_title.setText(post.getTitle());

        holder.contextView.setOnClickListener(new PostAdapter.OnPostClicked(post));

        Bitmap postPicture = post.getBitmapPostPicture(holder.contextView.getContext());
        Bitmap picture = post.getBitmapPicture(holder.contextView.getContext());
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
        private final View contextView;

        public PostViewHolder(View view) {
            super(view);
            contextView = view;
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

    private class OnPostClicked implements View.OnClickListener{
        final Post post;
        private OnPostClicked(final Post post){
            this.post = post;
        }

        @Override
        public void onClick(View v) {
            for (PostAdapter.OnPostClickedListener onPostClickedListener: onPostClickedListeners){
                onPostClickedListener.onPostClicked(post);
            }
        }
    }

    public void addOnPostClickedListener(PostAdapter.OnPostClickedListener onPostClickedListener){
        this.onPostClickedListeners.add(onPostClickedListener);
    }

    public void removeOnPostClickedListener(PostAdapter.OnPostClickedListener onPostClickedListener){
        this.onPostClickedListeners.remove(onPostClickedListener);
    }*/
}
