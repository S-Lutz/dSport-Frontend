package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.viewholder.EventViewHolder;
import com.omgproduction.dsport_application.aaRefactored.helper.DateConverter;
import com.omgproduction.dsport_application.aaRefactored.helper.GlideApp;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.SocialResultPair;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private ArrayList<SocialResultPair> eventNodes;
    private Context context;
    private onSocialItemClickedListener onSocialItemClickListener;

    public EventAdapter(Context context, ArrayList<SocialResultPair> eventNodes, onSocialItemClickedListener onSocialItemClickListener) {
        this.eventNodes = eventNodes;
        this.context = context;
        this.onSocialItemClickListener = onSocialItemClickListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v2 = inflater.inflate(R.layout.new_event_layout, parent, false);

        return new EventViewHolder(v2, onSocialItemClickListener);

    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int position) {
        SocialResultPair currentPair = eventNodes.get(position);
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

    @Override
    public int getItemCount() {
        return eventNodes.size();
    }
}