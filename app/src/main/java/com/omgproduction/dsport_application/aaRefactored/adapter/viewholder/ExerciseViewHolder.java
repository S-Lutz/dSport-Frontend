package com.omgproduction.dsport_application.aaRefactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;


public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView setType;

    public ExerciseViewHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.exercise_name);
        setType = (ImageView) itemView.findViewById(R.id.exercise_type);

    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public ImageView getSetType() {
        return setType;
    }

    public void setSetType(ImageView setType) {
        this.setType = setType;
    }
}