package com.omgproduction.dsport_application.refactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;

public class TimedSetViewHolder extends RecyclerView.ViewHolder {

    private TextView time_tv;

    public TimedSetViewHolder(View view) {
        super(view);
        time_tv = (TextView) view.findViewById(R.id.timed_exercise_unit_time);
    }

    public TextView getTime_tv() {
        return time_tv;
    }

    public void setTime_tv(TextView time_tv) {
        this.time_tv = time_tv;
    }
}