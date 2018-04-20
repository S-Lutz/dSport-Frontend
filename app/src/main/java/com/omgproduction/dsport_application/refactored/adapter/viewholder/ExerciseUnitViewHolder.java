package com.omgproduction.dsport_application.refactored.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;

public class ExerciseUnitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    onSocialItemClickedListener itemClickListener;

    private TextView name;
    private TextView date;
    private ImageView setType;

    public ExerciseUnitViewHolder(View itemView, onSocialItemClickedListener itemClickListener) {
        super(itemView);

        this.itemClickListener = itemClickListener;

        name = (TextView) itemView.findViewById(R.id.exercise_unit_name);
        date = (TextView) itemView.findViewById(R.id.exercise_unit_date);
        setType = (ImageView) itemView.findViewById(R.id.exercise_unit_type);

        name.setOnClickListener(this);
        date.setOnClickListener(this);
        setType.setOnClickListener(this);

    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public ImageView getSetType() {
        return setType;
    }

    public void setSetType(ImageView setType) {
        this.setType = setType;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}