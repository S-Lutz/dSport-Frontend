package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.WeightSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 23.03.2017.
 */

public class WeightSetAdapter  extends  RecyclerView.Adapter<WeightSetAdapter.WeightSetViewHolder> {


    private List<WeightSet> sets = new ArrayList<>();

    public WeightSetAdapter(List<WeightSet> sets) {
        this.sets = sets;
    }

    @Override
    public WeightSetAdapter.WeightSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_weight_set_item, parent, false);
        WeightSetAdapter.WeightSetViewHolder viewHolder = new WeightSetAdapter.WeightSetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeightSetAdapter.WeightSetViewHolder holder, int position) {
        WeightSet set = sets.get(position);

        holder.time_tv.setText(getHoursString(holder.itemView.getContext(), set.getTime())+" "+getMinutesString(holder.itemView.getContext(), set.getTime())+" "+getSecondsString(holder.itemView.getContext(), set.getTime()));
        holder.repeats_tv.setText(String.valueOf(set.getRepeats()));
        holder.weight_tv.setText(String.valueOf(set.getWeight()));
    }

    public String getSecondsString(Context context, long time){
        int seconds = (int) (time/1000);
        int rem = (int) (seconds%3600);
        int sec = rem%60;
        return (sec<10 ? "0" : "")+sec+context.getString(R.string.seconds);
    }

    public String getHoursString(Context context, long time){
        int seconds = (int) (time/1000);
        int hr = (int) (seconds/3600);
        return (hr!=0)?(hr<10 ? "0" : "")+hr+context.getString(R.string.hour):"";
    }

    public String getMinutesString(Context context, long time){
        int seconds = (int) (time/1000);
        int hr = (int) (seconds/3600);
        int rem = (int) (seconds%3600);
        int mn = rem/60;
        return (mn!=0&&hr!=0)?(mn<10 ? "0" : "")+mn+context.getString(R.string.minutes):"";
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class WeightSetViewHolder extends RecyclerView.ViewHolder {

        private TextView weight_tv, repeats_tv, time_tv;

        public WeightSetViewHolder(View view) {
            super(view);
            weight_tv = (TextView) view.findViewById(R.id.create_weight_exercise_unit_weight);
            repeats_tv = (TextView) view.findViewById(R.id.weight_exercise_unit_repeats);
            time_tv = (TextView) view.findViewById(R.id.timed_exercise_unit_time);
        }
    }
}
