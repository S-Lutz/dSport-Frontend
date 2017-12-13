package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.sets.AbstractSet;

import java.util.ArrayList;
import java.util.List;

public class TimeBasedSetAdapter extends RecyclerView.Adapter<TimeBasedSetAdapter.CreateTimedSetViewHolder> {

    private List<AbstractSet> sets = new ArrayList<>();

    public TimeBasedSetAdapter(List<AbstractSet> sets) {
        this.sets = sets;
    }

    @Override
    public TimeBasedSetAdapter.CreateTimedSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_create_time_set_layout, parent, false);
        TimeBasedSetAdapter.CreateTimedSetViewHolder viewHolder = new TimeBasedSetAdapter.CreateTimedSetViewHolder(view);
        return viewHolder;
    }



    public static class CreateTimedSetViewHolder extends RecyclerView.ViewHolder {

        private TextView time_tv;

        public CreateTimedSetViewHolder(View view) {
            super(view);
            time_tv = (TextView) view.findViewById(R.id.create_timed_exercise_unit_time);
        }
    }

    @Override
    public void onBindViewHolder(TimeBasedSetAdapter.CreateTimedSetViewHolder holder, int position) {
        final AbstractSet set = sets.get(position);
        holder.itemView.setOnLongClickListener(new TimeBasedSetAdapter.OnSetLongClicked(position, set));
        holder.time_tv.setText(String.valueOf(Integer.valueOf(set.getTime())/1000));
        holder.time_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                set.setTime(String.valueOf(s.toString().trim().isEmpty()?0:Long.parseLong(s.toString())*1000));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    private class OnSetLongClicked implements View.OnLongClickListener{

        public int position;
        public AbstractSet set;

        public OnSetLongClicked(int position, AbstractSet set) {
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
        void onLongClicked(int position, AbstractSet set);
    }

    private OnSetLongClickedListener onSetLongClickedListener;

    public void setOnSetLongClickedListener(OnSetLongClickedListener onSetLongClickedListener) {
        this.onSetLongClickedListener = onSetLongClickedListener;
    }
}