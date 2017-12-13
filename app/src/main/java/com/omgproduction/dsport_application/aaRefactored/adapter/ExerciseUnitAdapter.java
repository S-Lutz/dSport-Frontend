package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.ExerciseUnitViewHolder;
import com.omgproduction.dsport_application.aaRefactored.helper.DateConverter;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseUnitNode;

import java.util.ArrayList;

public class ExerciseUnitAdapter extends RecyclerView.Adapter<ExerciseUnitViewHolder> {

    private onSocialItemClickedListener onSocialItemClickListener;

    private ArrayList<ExerciseUnitNode> exerciseUnits;
    private Context context;

    public ExerciseUnitAdapter(Context context, ArrayList<ExerciseUnitNode> exerciseNodes, onSocialItemClickedListener onSocialItemClickListener) {
        this.context = context;
        this.exerciseUnits = exerciseNodes;
        this.onSocialItemClickListener = onSocialItemClickListener;
    }


    @Override
    public ExerciseUnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View exerciseUnitViewHolder = inflater.inflate(R.layout.item_custom_row_layout_exercise_unit, parent, false);
        return new ExerciseUnitViewHolder(exerciseUnitViewHolder, onSocialItemClickListener);
    }

    @Override
    public void onBindViewHolder(ExerciseUnitViewHolder holder, int position) {
        ExerciseUnitNode currentExercise = exerciseUnits.get(position);

        holder.getName().setText(currentExercise.getOfType().getName());

        holder.getDate().setText(DateConverter.convertDate(currentExercise.getCreated()));

        switch(currentExercise.getOfType().getSetType()){
            case "REPEAT":
                holder.getSetType().setImageResource(R.drawable.ic_repeat_exercise);
                break;
            case "DISTANCE":
                holder.getSetType().setImageResource(R.drawable.ic_distance_exercise);
                break;
            case "TIME":
                holder.getSetType().setImageResource(R.drawable.ic_timed_exercise);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return exerciseUnits.size();
    }
}