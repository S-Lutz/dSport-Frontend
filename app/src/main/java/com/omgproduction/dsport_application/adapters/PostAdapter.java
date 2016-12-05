package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.utils.DateUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Florian on 17.11.2016.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private ArrayList<Post> posts = new ArrayList<>();

    public interface OnPostClickedListener{
        void onPostClicked(PostViewHolder holder, Post p);
        void onPostLike(PostViewHolder holder, Post p);
        void onPostShare(PostViewHolder holder, Post p);
        void onPostComment(PostViewHolder holder, Post p);
    }

    private final ArrayList<OnPostClickedListener> onPostClickedListeners = new ArrayList<>();

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
        holder.tv_date.setText(DateUtils.convertString(holder.contextView.getContext(),post.getCreated()));
        holder.tv_shares.setText(post.getShareCount());
        holder.tv_comments.setText(post.getCommentCount());
        holder.tv_likes.setText(post.getLikeCount());
        holder.tv_text.setText(post.getText());
        holder.tv_username.setText(post.getUsername());
        holder.tv_title.setText(post.getTitle());

        holder.contextView.setOnClickListener(new OnPostClicked(holder,post));
        holder.tv_comments.setOnClickListener(new OnPostClicked(holder,post));
        holder.tv_likes.setOnClickListener(new OnPostClicked(holder,post));
        holder.tv_shares.setOnClickListener(new OnPostClicked(holder,post));

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

        private ImageView iv_picture, iv_post_picture, iv_post_picture_overlay;
        private TextView tv_username, tv_text, tv_likes, tv_comments, tv_shares, tv_date, tv_title;
        private final View contextView;
        private LinearLayout post_buttons;
        private RelativeLayout post_layout;

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
            post_buttons = (LinearLayout) view.findViewById(R.id.post_option_container);
            iv_post_picture_overlay = (ImageView) view.findViewById(R.id.post_picture_overlay);
            post_layout = (RelativeLayout) view.findViewById(R.id.post_relative_layout);
        }

        public ImageView getIv_picture() {
            return iv_picture;
        }

        public ImageView getIv_post_picture() {
            return iv_post_picture;
        }

        public TextView getTv_username() {
            return tv_username;
        }

        public TextView getTv_text() {
            return tv_text;
        }

        public TextView getTv_likes() {
            return tv_likes;
        }

        public TextView getTv_comments() {
            return tv_comments;
        }

        public TextView getTv_shares() {
            return tv_shares;
        }

        public TextView getTv_date() {
            return tv_date;
        }

        public TextView getTv_title() {
            return tv_title;
        }

        public View getContextView() {
            return contextView;
        }

        public LinearLayout getPost_buttons() {
            return post_buttons;
        }

        public ImageView getIv_post_picture_overlay() {
            return iv_post_picture_overlay;
        }

        public RelativeLayout getPost_layout() {
            return post_layout;
        }
    }

    private class OnPostClicked implements View.OnClickListener{
        final Post post;
        final PostViewHolder holder;
        private OnPostClicked(final PostViewHolder holder, final Post post){
            this.holder = holder;
            this.post = post;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_like_count:
                    for (OnPostClickedListener onPostClickedListener: onPostClickedListeners){
                        onPostClickedListener.onPostLike(holder,post);
                    }
                    break;
                case R.id.post_share_count:
                    for (OnPostClickedListener onPostClickedListener: onPostClickedListeners){
                        onPostClickedListener.onPostShare(holder,post);
                    }
                    break;
                case R.id.post_comment_count:
                    for (OnPostClickedListener onPostClickedListener: onPostClickedListeners){
                        onPostClickedListener.onPostComment(holder,post);
                    }
                    break;
                default:
                    for (OnPostClickedListener onPostClickedListener: onPostClickedListeners){
                        onPostClickedListener.onPostClicked(holder,post);
                    }
                    break;
            }
        }
    }

    public void addOnPostClickedListener(OnPostClickedListener onPostClickedListener){
        this.onPostClickedListeners.add(onPostClickedListener);
    }

    public void removeOnPostClickedListener(OnPostClickedListener onPostClickedListener){
        this.onPostClickedListeners.remove(onPostClickedListener);
    }
}
