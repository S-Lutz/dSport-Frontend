package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.EventViewHolder;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.ExerciseViewHolder;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.PostViewHolder;
import com.omgproduction.dsport_application.aaRefactored.helper.DateConverter;
import com.omgproduction.dsport_application.aaRefactored.helper.GlideApp;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.PostNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.SocialResultPair;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.ViewHolder;

public class PinboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SocialResultPair> socialNodes;
    private Context context;
    private onSocialItemClickedListener itemClickListener;
    private final int POSTNODE = 0, EVENTNODE = 1, EXERCISEUNITNODE = 2;


    public PinboardAdapter(Context context, ArrayList<SocialResultPair> socialNodes, onSocialItemClickedListener itemClickListener) {
        this.socialNodes = socialNodes;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POSTNODE:
                View v1 = inflater.inflate(R.layout.new_post_layout, parent, false);
                return new PostViewHolder(v1, itemClickListener);
            case EVENTNODE:
                View v2 = inflater.inflate(R.layout.new_event_layout, parent, false);
                return new EventViewHolder(v2, itemClickListener);
            case EXERCISEUNITNODE:
                //View v3 = inflater.inflate(R.layout.layout_viewholder2, parent, false);
                //viewHolder = new ExerciseViewHolder(v3);
                break;
            default:
                break;
        }

        Log.e("ERROR", "UNKNOWN VIEW_TYPE");
        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case POSTNODE:
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                configurePostViewHolder(postViewHolder, position);
                break;
            case EVENTNODE:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                configureEventViewHolder(eventViewHolder, position);
                break;
            case EXERCISEUNITNODE:
                ExerciseViewHolder exerciseViewHolder = (ExerciseViewHolder) holder;
                configureExerciseViewHolder(exerciseViewHolder, position);
                break;
            default:
                break;
        }
    }

    private void configureExerciseViewHolder(ExerciseViewHolder exerciseViewHolder, int position) {
    }

    private void configureEventViewHolder(EventViewHolder eventViewHolder, int position) {
        SocialResultPair currentPair =socialNodes.get(position);
        EventNode currentEvent = (EventNode) currentPair.getSocialNode();
        UserNode eventOwner = currentPair.getUserNode();

        if (eventOwner.getPicture() != null) {
            GlideApp
                    .with(context)
                    .load(eventOwner.getPicture())
                    .circleCrop()
                    .into(eventViewHolder.getEventProfilePic());
        }

        eventViewHolder.getEventProfileheaderText().setText(eventOwner.getUsername());

        eventViewHolder.getEventProfileheaderSubText().setText(DateConverter.convertDate(currentEvent.getCreated()));


        if (currentEvent.getPicture() != null) {
            eventViewHolder.getEventPicture().setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(currentEvent.getPicture())
                    .centerCrop()
                    .into(eventViewHolder.getEventPicture());
        }


        eventViewHolder.getEventTitle().setText(currentEvent.getTitle());

        eventViewHolder.getEventDate().setText(currentEvent.getDate());
        eventViewHolder.getEventLocationName().setText(currentEvent.getLocationName());
        eventViewHolder.getEventLocationAdress().setText(currentEvent.getLocationAdress());

        eventViewHolder.getEventText().setText(currentEvent.getText());

        if (currentPair.isLikesSocialNode())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                eventViewHolder.getEventLikebtn().setColorFilter(new LightingColorFilter(context.getColor(R.color.colorPrimary), context.getColor(R.color.colorPrimary)));
            }else
                eventViewHolder.getEventLikebtn().setColorFilter(new LightingColorFilter(Color.RED, Color.RED));

        if (currentEvent.getParticipating())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                eventViewHolder.getParticipatebtn().setColorFilter(new LightingColorFilter(context.getColor(R.color.colorPrimary), context.getColor(R.color.colorPrimary)));
            }else
                eventViewHolder.getParticipatebtn().setColorFilter(new LightingColorFilter(Color.RED, Color.RED));

        eventViewHolder.getEventparticipateCount().setText(currentEvent.getParticipates());
        eventViewHolder.getEventlikeCount().setText(currentEvent.getLikeCount());
        eventViewHolder.getEventCommentCount().setText(currentEvent.getCommentCount());

    }

    private void configurePostViewHolder(PostViewHolder postViewHolder, int position) {
        SocialResultPair currentPair = socialNodes.get(position);
        PostNode currentPost = (PostNode) currentPair.getSocialNode();
        UserNode postOwner = currentPair.getUserNode();

        if (postOwner.getPicture() != null) {
            GlideApp
                    .with(context)
                    .load(postOwner.getPicture())
                    .circleCrop()
                    .into(postViewHolder.getPostProfilePic());
        }

        postViewHolder.getPostProfileheaderText().setText(postOwner.getUsername());

        postViewHolder.getPostProfileheaderSubText().setText(DateConverter.convertDate(currentPost.getCreated()));


        if (currentPost.getPicture() != null) {
            postViewHolder.getPostPicture().setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .load(currentPost.getPicture())
                    .centerCrop()
                    .into(postViewHolder.getPostPicture());
        }


        postViewHolder.getPostTitle().setText(currentPost.getTitle());

        postViewHolder.getPostText().setText(currentPost.getText());

        if (currentPair.isLikesSocialNode())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                postViewHolder.getPostLikebtn().setColorFilter(new LightingColorFilter(context.getColor(R.color.colorPrimary), context.getColor(R.color.colorPrimary)));
            }else
                postViewHolder.getPostLikebtn().setColorFilter(new LightingColorFilter(Color.RED, Color.RED));

        postViewHolder.getPostlikeCount().setText(currentPost.getLikeCount());
        postViewHolder.getPostCommentCount().setText(currentPost.getCommentCount());

    }

    @Override
    public int getItemCount() {
        return socialNodes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (socialNodes.get(position).getSocialNode() instanceof PostNode) {
            return POSTNODE;
        } else if (socialNodes.get(position).getSocialNode() instanceof EventNode) {
            return EVENTNODE;
        } else if (socialNodes.get(position).getSocialNode() instanceof ExerciseUnitNode) {
            return EXERCISEUNITNODE;
        }
        return -1;
    }
}