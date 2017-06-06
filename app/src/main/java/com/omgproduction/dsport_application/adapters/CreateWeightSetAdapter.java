package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.WeightSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 23.03.2017.
 */

public class CreateWeightSetAdapter  extends  RecyclerView.Adapter<CreateWeightSetAdapter.CreateWeightSetViewHolder> {


    private List<WeightSet> sets = new ArrayList<>();

    public CreateWeightSetAdapter(List<WeightSet> sets) {
        this.sets = sets;
    }

    @Override
    public CreateWeightSetAdapter.CreateWeightSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_weight_set_layout, parent, false);
        CreateWeightSetAdapter.CreateWeightSetViewHolder viewHolder = new CreateWeightSetAdapter.CreateWeightSetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CreateWeightSetAdapter.CreateWeightSetViewHolder holder, int position) {
        final WeightSet set = sets.get(position);
        holder.itemView.setOnLongClickListener(new OnSetLongClicked(position, set));
        holder.time_tv.setText(getHoursString(holder.itemView.getContext(), set.getTime()) + " " + getMinutesString(holder.itemView.getContext(), set.getTime()) + " " + getSecondsString(holder.itemView.getContext(), set.getTime()));
        holder.repeats_tv.setText(String.valueOf(set.getRepeats()));
        holder.repeats_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = holder.repeats_tv.getText().toString();
                set.setRepeats(string.trim().isEmpty()?0:Integer.parseInt(string));
            }
        });
        holder.weight_tv.setText(String.valueOf(set.getWeight()));
        holder.weight_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = holder.weight_tv.getText().toString();
                set.setWeight(string.trim().isEmpty()?0:Float.parseFloat(string));
            }
        });
    }

    public String getSecondsString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int rem = (int) (seconds % 3600);
        int sec = rem % 60;
        return (sec < 10 ? "0" : "") + sec + context.getString(R.string.seconds);
    }

    public String getHoursString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int hr = (int) (seconds / 3600);
        return (hr != 0) ? (hr < 10 ? "0" : "") + hr + context.getString(R.string.hour) : "";
    }

    public String getMinutesString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int hr = (int) (seconds / 3600);
        int rem = (int) (seconds % 3600);
        int mn = rem / 60;
        return (mn != 0 && hr != 0) ? (mn < 10 ? "0" : "") + mn + context.getString(R.string.minutes) : "";
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class CreateWeightSetViewHolder extends RecyclerView.ViewHolder {

        private TextView time_tv;
        private EditText weight_tv, repeats_tv;

        public CreateWeightSetViewHolder(View view) {
            super(view);
            weight_tv = (EditText) view.findViewById(R.id.create_weight_exercise_unit_weight);
            repeats_tv = (EditText) view.findViewById(R.id.create_weight_exercise_unit_repeats);
            time_tv = (TextView) view.findViewById(R.id.create_timed_exercise_unit_time);
        }
    }

    private class OnSetLongClicked implements View.OnLongClickListener{

        public int position;
        public WeightSet set;

        public OnSetLongClicked(int position, WeightSet set) {
            this.position = position;
            this.set = set;
        }

        @Override
        public boolean onLongClick(View v) {
            if(onSetLongClickedListener!=null){
                onSetLongClickedListener.onLongClicked(position, set);
            }
            return false;
        }
    }

    public interface OnSetLongClickedListener{
        void onLongClicked(int position, WeightSet set);
    }

    private OnSetLongClickedListener onSetLongClickedListener;

    public void setOnSetLongClickedListener(OnSetLongClickedListener onSetLongClickedListener) {
        this.onSetLongClickedListener = onSetLongClickedListener;
    }
}