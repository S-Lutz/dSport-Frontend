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
import com.omgproduction.dsport_application.models.TimedSet;
import com.omgproduction.dsport_application.models.WeightSet;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 24.03.2017.
 */

public class CreateTimedSetAdapter  extends  RecyclerView.Adapter<CreateTimedSetAdapter.CreateTimedSetViewHolder> {


    private List<TimedSet> sets = new ArrayList<>();

    public CreateTimedSetAdapter(List<TimedSet> sets) {
        this.sets = sets;
    }

    @Override
    public CreateTimedSetAdapter.CreateTimedSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_tiemd_set_layout, parent, false);
        CreateTimedSetAdapter.CreateTimedSetViewHolder viewHolder = new CreateTimedSetAdapter.CreateTimedSetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CreateTimedSetAdapter.CreateTimedSetViewHolder holder, int position) {
        final TimedSet set = sets.get(position);
        holder.itemView.setOnLongClickListener(new CreateTimedSetAdapter.OnSetLongClicked(position, set));
        holder.time_tv.setText(String.valueOf(set.getTime()/1000));
        holder.time_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                set.setTime(s.toString().trim().isEmpty()?0:Long.parseLong(s.toString())*1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class CreateTimedSetViewHolder extends RecyclerView.ViewHolder {

        private TextView time_tv;

        public CreateTimedSetViewHolder(View view) {
            super(view);
            time_tv = (TextView) view.findViewById(R.id.create_timed_exercise_unit_time);
        }
    }

    private class OnSetLongClicked implements View.OnLongClickListener{

        public int position;
        public TimedSet set;

        public OnSetLongClicked(int position, TimedSet set) {
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
        void onLongClicked(int position, TimedSet set);
    }

    private CreateTimedSetAdapter.OnSetLongClickedListener onSetLongClickedListener;

    public void setOnSetLongClickedListener(CreateTimedSetAdapter.OnSetLongClickedListener onSetLongClickedListener) {
        this.onSetLongClickedListener = onSetLongClickedListener;
    }
}
