package com.omgproduction.dsport_application.aaRefactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;

public class DistanceBasedSetViewHolder extends RecyclerView.ViewHolder {

    private TextView time;
    private TextView distance;

    public DistanceBasedSetViewHolder(View itemView) {
        super(itemView);

        time = (TextView) itemView.findViewById(R.id.weight_exercise_unit_repeats);
        distance = (TextView) itemView.findViewById(R.id.timed_exercise_unit_time);
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getDistance() {
        return distance;
    }

    public void setDistance(TextView distance) {
        this.distance = distance;
    }
}