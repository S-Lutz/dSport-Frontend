package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.DistanceExerciseUnit;
import com.omgproduction.dsport_application.models.ExerciseUnit;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.TimedExerciseUnit;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.models.WeightExerciseUnit;
import com.omgproduction.dsport_application.services.ExerciseService;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.sql.Time;
import java.util.List;

/**
 * Created by Florian on 22.03.2017.
 */

public class ExerciseUnitAdapter  extends RecyclerView.Adapter<ExerciseUnitAdapter.AbstractExerciseUnitViewHolder> {

    private List<ExerciseUnit> exerciseUnits;

    public ExerciseUnitAdapter(List<ExerciseUnit> exerciseUnits) {
        this.exerciseUnits = exerciseUnits;
    }

    abstract class AbstractExerciseUnitViewHolder extends RecyclerView.ViewHolder{

        protected TextView likeCount, title, username, date;
        protected ImageView userpic;

        public AbstractExerciseUnitViewHolder(View itemView) {
            super(itemView);

            likeCount = (TextView) itemView.findViewById(R.id.exercise_unit_like_count);
            title = (TextView) itemView.findViewById(R.id.exercise_unit_title);
            username = (TextView) itemView.findViewById(R.id.exercise_unit_username);
            date = (TextView) itemView.findViewById(R.id.distance_unit_date);
            userpic = (ImageView) itemView.findViewById(R.id.exercise_unit_user_picture);

        }
    }

    class ViewHolder0 extends AbstractExerciseUnitViewHolder{

        private RecyclerView recyclerView;
        private WeightSetAdapter adapter;
        private LinearLayoutManager layoutManager;

        public ViewHolder0(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.exercise_unit_sets);

        }
    }
    class ViewHolder1 extends AbstractExerciseUnitViewHolder {

        private TextView time, distance;

        public ViewHolder1(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.exercise_unit_time);
            distance = (TextView) itemView.findViewById(R.id.distance_unit_distance);

        }
    }

    class ViewHolder2 extends AbstractExerciseUnitViewHolder{

        private RecyclerView recyclerView;
        private TimedSetAdapter adapter;
        private LinearLayoutManager layoutManager;

        public ViewHolder2(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.exercise_unit_sets);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return exerciseUnits.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return exerciseUnits.size();
    }

    @Override
    public ExerciseUnitAdapter.AbstractExerciseUnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_weight_exercise_unit,parent,false);
                ViewHolder0 viewHolder0 = new ViewHolder0(view0);
                return viewHolder0;
            case 1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_distance_exercise_unit_list_item,parent,false);
                ViewHolder1 viewHolder1 = new ViewHolder1(view1);
                return viewHolder1;
            case 2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_weight_exercise_unit,parent,false);
                ViewHolder2 viewHolder2 = new ViewHolder2(view2);
                return viewHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ExerciseUnitAdapter.AbstractExerciseUnitViewHolder holder, final int position) {
        ExerciseUnit unit = exerciseUnits.get(position);

        DateConverter converter = new DateConverter();

        holder.userpic.setImageBitmap(BitmapUtils.getBitmapFromString(unit.getUserPic()));
        holder.username.setText(unit.getUsername());
        holder.likeCount.setText(unit.getLikeString(holder.itemView.getContext()));
        holder.likeCount.setOnClickListener(new OnExerciseUnitClick(unit,holder));
        holder.title.setText(unit.getExerciseTitle());
        holder.date.setText(converter.convertString(holder.itemView.getContext(),unit.getDate()));

        switch (unit.getType()) {
            case 0:
                WeightExerciseUnit wUnit = (WeightExerciseUnit) unit;
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                viewHolder0.layoutManager = new LinearLayoutManager(viewHolder0.itemView.getContext());
                viewHolder0.recyclerView.setLayoutManager(viewHolder0.layoutManager);
                viewHolder0.recyclerView.setHasFixedSize(true);
                viewHolder0.adapter = new WeightSetAdapter(wUnit.getSets());
                viewHolder0.recyclerView.setAdapter(viewHolder0.adapter);
                break;
            case 1:
                DistanceExerciseUnit dUnit = (DistanceExerciseUnit) unit;
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.distance.setText(String.valueOf(dUnit.getDistance()+viewHolder1.itemView.getContext().getString(R.string.kilometer)));
                viewHolder1.time.setText(getHoursString(holder.itemView.getContext(), dUnit.getTime())+" "+getMinutesString(holder.itemView.getContext(), dUnit.getTime())+" "+getSecondsString(holder.itemView.getContext(), dUnit.getTime()));

                break;

            case 2:
                TimedExerciseUnit tUnit = (TimedExerciseUnit) unit;
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.layoutManager = new LinearLayoutManager(viewHolder2.itemView.getContext());
                viewHolder2.recyclerView.setLayoutManager(viewHolder2.layoutManager);
                viewHolder2.recyclerView.setHasFixedSize(true);
                viewHolder2.adapter = new TimedSetAdapter(tUnit.getSets());
                viewHolder2.recyclerView.setAdapter(viewHolder2.adapter);
                break;
        }
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

    public class OnExerciseUnitClick implements View.OnClickListener{

        private final ExerciseUnit exerciseUnit;
        private final AbstractExerciseUnitViewHolder viewHolder;

        public OnExerciseUnitClick(ExerciseUnit exerciseUnit, AbstractExerciseUnitViewHolder viewHolder) {
            this.exerciseUnit = exerciseUnit;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            UserService userService = new UserService(v.getContext());
            User localUser = userService.getLocalUser();

            ExerciseService exerciseService = new ExerciseService(v.getContext());
            exerciseService.likeExerciseUnit(localUser.getId(), exerciseUnit.getId(), new RequestFuture<LikeResult>(){
                @Override
                public void onSuccess(LikeResult result) {
                    exerciseUnit.setLikeCount(Integer.parseInt(result.getLikeCount()));
                    exerciseUnit.setLiked(result.isLiked());
                    viewHolder.likeCount.setText(exerciseUnit.getLikeString(viewHolder.itemView.getContext()));
                }
            });

        }
    }

}
