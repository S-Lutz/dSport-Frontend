package com.omgproduction.dsport_application.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.Like;

import java.util.ArrayList;

/**
 * Created by Florian on 01.12.2016.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeViewHolder> {

    private ArrayList<Like> likes = new ArrayList<>();

    public interface OnLikeClickedListener {
        void onLikeSelected(Like like);
    }

    private final ArrayList<LikeAdapter.OnLikeClickedListener> onLikeClickedListeners = new ArrayList<>();

    public LikeAdapter(ArrayList<Like> likes) {
        this.likes = likes;
    }

    @Override
    public LikeAdapter.LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_like, parent, false);
        LikeAdapter.LikeViewHolder viewHolder = new LikeAdapter.LikeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LikeAdapter.LikeViewHolder holder, int position) {
        Like like = likes.get(position);
        holder.tv_username.setText(like.getUsername());
        holder.tv_username.setOnClickListener(new OnLikeClicked(like));
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    public static class LikeViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_username;

        public LikeViewHolder(View view) {
            super(view);
            tv_username = (AppCompatTextView) view.findViewById(R.id.like_username);
        }
        public TextView getTv_username() {
            return tv_username;
        }
    }

    private class OnLikeClicked implements View.OnClickListener {
        final Like like;

        private OnLikeClicked(final Like like) {
            this.like = like;
        }

        @Override
        public void onClick(View v) {
            for (OnLikeClickedListener onLikeClickedListener : onLikeClickedListeners) {
                onLikeClickedListener.onLikeSelected(like);
            }
        }
    }

    public void addOnLikeClickedListener(OnLikeClickedListener onLikeClickedListener) {
        this.onLikeClickedListeners.add(onLikeClickedListener);
    }

    public void removeOnLikeClickedListener(OnLikeClickedListener onLikeClickedListener) {
        this.onLikeClickedListeners.remove(onLikeClickedListener);
    }
}
