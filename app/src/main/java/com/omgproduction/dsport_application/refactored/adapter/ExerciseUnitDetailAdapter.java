package com.omgproduction.dsport_application.refactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.adapter.viewholder.DistanceBasedSetViewHolder;
import com.omgproduction.dsport_application.refactored.adapter.viewholder.RepeatBasedSetViewHolder;
import com.omgproduction.dsport_application.refactored.adapter.viewholder.TimedSetViewHolder;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.AbstractSet;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.DistanceBasedSet;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.RepeatBasedSet;

import java.util.ArrayList;

public class ExerciseUnitDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AbstractSet> sets;
    private Context context;

    private final int ABSTRACTSET = 0, REPEAPSET = 1, DISTANCESET = 2;

    public ExerciseUnitDetailAdapter(Context context, ArrayList<AbstractSet> sets) {
        this.sets = sets;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case DISTANCESET:
                View v1 = inflater.inflate(R.layout.new_layout_distance_based_item, parent, false);
                return new DistanceBasedSetViewHolder(v1);
            case REPEAPSET:
                View v2 = inflater.inflate(R.layout.layout_weight_set_item, parent, false);
                return new RepeatBasedSetViewHolder(v2);
            case ABSTRACTSET:
                View v3 = inflater.inflate(R.layout.layout_timed_set_item, parent, false);
                return new TimedSetViewHolder(v3);
            default:
                break;
        }

        Log.e("ERROR", "UNKNOWN VIEW_TYPE");
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case DISTANCESET:
                DistanceBasedSetViewHolder distanceBasedSetViewHolder = (DistanceBasedSetViewHolder) holder;
                configureDistanceBased(distanceBasedSetViewHolder, position);
                break;
            case REPEAPSET:
                RepeatBasedSetViewHolder repeatBasedSetViewHolder = (RepeatBasedSetViewHolder) holder;
                configureRepeatBased(repeatBasedSetViewHolder, position);
                break;
            case ABSTRACTSET:
                TimedSetViewHolder timedSetViewHolder = (TimedSetViewHolder) holder;
                configureTimeBased(timedSetViewHolder, position);
                break;
            default:
                break;
        }
    }

    private void configureTimeBased(TimedSetViewHolder timedSetViewHolder, int position) {
        AbstractSet abstractSet = sets.get(position);

        timedSetViewHolder.getTime_tv().setText(abstractSet.getTime());
    }

    private void configureRepeatBased(RepeatBasedSetViewHolder repeatBasedSetViewHolder, int position) {
        RepeatBasedSet abstractSet = (RepeatBasedSet) sets.get(position);
        repeatBasedSetViewHolder.getTime_tv().setText(abstractSet.getTime());
        repeatBasedSetViewHolder.getWeight_tv().setText(abstractSet.getWeight());
        repeatBasedSetViewHolder.getRepeats_tv().setText(abstractSet.getRepeats());
    }

    private void configureDistanceBased(DistanceBasedSetViewHolder distanceBasedSetViewHolder, int position) {
        DistanceBasedSet abstractSet = (DistanceBasedSet) sets.get(position);

        distanceBasedSetViewHolder.getDistance().setText(abstractSet.getDistance());
        distanceBasedSetViewHolder.getTime().setText(abstractSet.getTime());
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (sets.get(position) instanceof RepeatBasedSet) {
            return REPEAPSET;
        } else if (sets.get(position) instanceof DistanceBasedSet) {
            return DISTANCESET;
        } else if (sets.get(position) != null) {
            return ABSTRACTSET;
        }
        return -1;
    }
}