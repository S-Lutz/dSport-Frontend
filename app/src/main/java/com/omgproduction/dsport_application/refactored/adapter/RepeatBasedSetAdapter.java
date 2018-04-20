package com.omgproduction.dsport_application.refactored.adapter;

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
import com.omgproduction.dsport_application.refactored.models.nodes.sets.RepeatBasedSet;

import java.util.ArrayList;
import java.util.List;

public class RepeatBasedSetAdapter extends RecyclerView.Adapter<RepeatBasedSetAdapter.CreateRepeatBasedViewHolder> {

    private List<RepeatBasedSet> sets = new ArrayList<>();

    public RepeatBasedSetAdapter(List<RepeatBasedSet> sets) {
        this.sets = sets;
    }

    public static class CreateRepeatBasedViewHolder extends RecyclerView.ViewHolder {

        private TextView time_tv;
        private EditText weight_tv, repeats_tv;

        public CreateRepeatBasedViewHolder(View view) {
            super(view);
            weight_tv = (EditText) view.findViewById(R.id.create_weight_exercise_unit_weight);
            repeats_tv = (EditText) view.findViewById(R.id.create_weight_exercise_unit_repeats);
            time_tv = (TextView) view.findViewById(R.id.create_timed_exercise_unit_time);
        }
    }


    @Override
    public RepeatBasedSetAdapter.CreateRepeatBasedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_create_repeat_set_layout, parent, false);
        RepeatBasedSetAdapter.CreateRepeatBasedViewHolder viewHolder = new RepeatBasedSetAdapter.CreateRepeatBasedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RepeatBasedSetAdapter.CreateRepeatBasedViewHolder holder, int position) {
        final RepeatBasedSet set = sets.get(position);
        holder.itemView.setOnLongClickListener(new OnSetLongClicked(position, set));
        holder.time_tv.setText(getHoursString(holder.itemView.getContext(), Long.valueOf(set.getTime())) + " " + getMinutesString(holder.itemView.getContext(), Long.valueOf(set.getTime())) + " " + getSecondsString(holder.itemView.getContext(), Long.valueOf(set.getTime())));
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
                set.setRepeats(String.valueOf(string.trim().isEmpty()?0:Integer.parseInt(string)));
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
                set.setWeight(String.valueOf(string.trim().isEmpty()?0:Float.parseFloat(string)));
            }
        });
    }

    public String getSecondsString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int rem =  (seconds % 3600);
        int sec = rem % 60;
        return (sec < 10 ? "0" : "") + sec + context.getString(R.string.seconds);
    }

    public String getHoursString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int hr = (seconds / 3600);
        return (hr != 0) ? (hr < 10 ? "0" : "") + hr + context.getString(R.string.hour) : "";
    }

    public String getMinutesString(Context context, long time) {
        int seconds = (int) (time / 1000);
        int hr =  (seconds / 3600);
        int rem =  (seconds % 3600);
        int mn = rem / 60;
        return (mn != 0 && hr != 0) ? (mn < 10 ? "0" : "") + mn + context.getString(R.string.minutes) : "";
    }

    private class OnSetLongClicked implements View.OnLongClickListener{

        public int position;
        public RepeatBasedSet set;

        public OnSetLongClicked(int position, RepeatBasedSet set) {
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
        void onLongClicked(int position, RepeatBasedSet set);
    }

    private OnSetLongClickedListener onSetLongClickedListener;

    public void setOnSetLongClickedListener(OnSetLongClickedListener onSetLongClickedListener) {
        this.onSetLongClickedListener = onSetLongClickedListener;
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }
}