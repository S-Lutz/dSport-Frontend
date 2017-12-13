package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.ExerciseViewHolder;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseNode;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private ArrayList<ExerciseNode> exerciseNodes;
    private Context context;

    public ExerciseAdapter(Context context, ArrayList<ExerciseNode> exerciseNodes) {
        this.exerciseNodes = exerciseNodes;
        this.context = context;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View exerciseViewHolder = inflater.inflate(R.layout.item_custom_row_layout_exercise, parent, false);
        return new ExerciseViewHolder(exerciseViewHolder);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        ExerciseNode currentExercise = exerciseNodes.get(position);

        holder.getName().setText(currentExercise.getName());

        switch(currentExercise.getSetType()){
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
        return exerciseNodes.size();
    }
}