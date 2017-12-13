package com.omgproduction.dsport_application.aaRefactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;

public class RepeatBasedSetViewHolder extends RecyclerView.ViewHolder {

    private TextView time_tv;
    private TextView weight_tv, repeats_tv;

    public RepeatBasedSetViewHolder(View view) {
        super(view);
        weight_tv = (TextView) view.findViewById(R.id.weight_exercise_unit_weight);
        repeats_tv = (TextView) view.findViewById(R.id.weight_exercise_unit_repeats);
        time_tv = (TextView) view.findViewById(R.id.timed_exercise_unit_time);
    }

    public TextView getTime_tv() {
        return time_tv;
    }

    public void setTime_tv(TextView time_tv) {
        this.time_tv = time_tv;
    }

    public TextView getWeight_tv() {
        return weight_tv;
    }

    public void setWeight_tv(TextView weight_tv) {
        this.weight_tv = weight_tv;
    }

    public TextView getRepeats_tv() {
        return repeats_tv;
    }

    public void setRepeats_tv(EditText repeats_tv) {
        this.repeats_tv = repeats_tv;
    }
}
