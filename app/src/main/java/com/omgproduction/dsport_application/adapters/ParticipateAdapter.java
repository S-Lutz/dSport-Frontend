package com.omgproduction.dsport_application.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.Participate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 01.12.2016.
 */

public class ParticipateAdapter extends RecyclerView.Adapter<ParticipateAdapter.ParticipateViewHolder> {

    private List<Participate> participates = new ArrayList<>();

    public interface OnParticipateClickedListener {
        void onParticipateSelected(Participate participate);
    }

    private final ArrayList<ParticipateAdapter.OnParticipateClickedListener> onParticipateClickedListeners = new ArrayList<>();

    public ParticipateAdapter(List<Participate> participates) {
        this.participates = participates;
    }

    @Override
    public ParticipateAdapter.ParticipateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_participate, parent, false);
        ParticipateAdapter.ParticipateViewHolder viewHolder = new ParticipateAdapter.ParticipateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParticipateAdapter.ParticipateViewHolder holder, int position) {
        Participate participate = participates.get(position);
        holder.tv_username.setText(participate.getUsername());
        holder.tv_username.setOnClickListener(new OnParticipateClicked(participate));
    }

    @Override
    public int getItemCount() {
        return participates.size();
    }

    public static class ParticipateViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_username;

        public ParticipateViewHolder(View view) {
            super(view);
            tv_username = (AppCompatTextView) view.findViewById(R.id.participate_username);
        }
        public TextView getTv_username() {
            return tv_username;
        }
    }

    private class OnParticipateClicked implements View.OnClickListener {
        final Participate participate;

        private OnParticipateClicked(final Participate participate) {
            this.participate = participate;
        }

        @Override
        public void onClick(View v) {
            for (OnParticipateClickedListener onParticipateClickedListener : onParticipateClickedListeners) {
                onParticipateClickedListener.onParticipateSelected(participate);
            }
        }
    }

    public void addOnParticipateClickedListener(OnParticipateClickedListener onParticipateClickedListener) {
        this.onParticipateClickedListeners.add(onParticipateClickedListener);
    }

    public void removeOnParticipateClickedListener(OnParticipateClickedListener onParticipateClickedListener) {
        this.onParticipateClickedListeners.remove(onParticipateClickedListener);
    }
}
