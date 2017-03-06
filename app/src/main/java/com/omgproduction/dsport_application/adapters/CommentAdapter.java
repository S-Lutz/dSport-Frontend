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
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 01.12.2016.
 */

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{


    private List<Comment> comments = new ArrayList<>();

    private final ArrayList<OnLikeClickedListener> onLikeClickedListeners = new ArrayList<>();

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public interface OnLikeClickedListener{
        void onLikeComment(Comment comment, CommentViewHolder holder);
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment,parent,false);
        CommentAdapter.CommentViewHolder viewHolder = new CommentAdapter.CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        DateConverter dateConverter = new DateConverter();

        holder.tv_date.setText(dateConverter.convertString(comment.getCreated()));
        holder.tv_likes.setText(comment.getLikeString(holder.contextView.getContext()));
        holder.tv_text.setText(comment.getText());
        holder.tv_username.setText(comment.getUsername());

        holder.tv_likes.setOnClickListener(new OnLikeClicked(comment, holder));

        Bitmap commentPicture = comment.getBitmapCommentPicture(holder.contextView.getContext());
        if(commentPicture!=null){
            holder.iv_comment_picture.setVisibility(View.VISIBLE);
            holder.iv_comment_picture.setImageBitmap(commentPicture);
        }else{
            holder.iv_comment_picture.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_comment_picture;
        private TextView tv_username, tv_text, tv_likes, tv_date;
        private final View contextView;
        private Context context;

        public CommentViewHolder(View view) {
            super(view);
            contextView = view;
            iv_comment_picture = (ImageView) view.findViewById(R.id.comment_picture);
            tv_username = (TextView) view.findViewById(R.id.comment_username);
            tv_text = (TextView) view.findViewById(R.id.comment_text);
            tv_likes = (TextView) view.findViewById(R.id.comment_like_count);
            tv_date = (TextView) view.findViewById(R.id.comment_date);
            context = view.getContext();
        }

        public ImageView getIv_comment_picture() {
            return iv_comment_picture;
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

        public TextView getTv_date() {
            return tv_date;
        }

        public View getContextView() {
            return contextView;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }

    private class OnLikeClicked implements View.OnClickListener{
        final Comment comment;
        final CommentViewHolder holder;
        private OnLikeClicked(final Comment comment, final CommentViewHolder holder){
            this.comment = comment;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            for (OnLikeClickedListener onLikeClickedListener: onLikeClickedListeners){
                onLikeClickedListener.onLikeComment(comment, holder);
            }
        }
    }

    public void addOnLikeClickedListener(OnLikeClickedListener onLikeClickedListener){
        this.onLikeClickedListeners.add(onLikeClickedListener);
    }


    public void removeOnLikeClickedListener(OnLikeClickedListener onLikeClickedListener){
        this.onLikeClickedListeners.remove(onLikeClickedListener);
    }
}
