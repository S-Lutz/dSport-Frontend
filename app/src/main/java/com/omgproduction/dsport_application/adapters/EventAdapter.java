package com.omgproduction.dsport_application.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strik on 08.03.2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Event> events = new ArrayList<>();

    public interface OnEventClickedListener{
        void onEventClicked(EventViewHolder holder, Event e);
        void onEventLike(EventViewHolder holder, Event e);
        void onParticipateEvent(EventViewHolder holder, Event e);
        void onEventShare(EventViewHolder holder, Event e);
        void onEventComment(EventViewHolder holder, Event e);
    }

    private final ArrayList<OnEventClickedListener> onEventClickedListeners = new ArrayList<>();

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event,parent,false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);

        DateConverter converter = new DateConverter();

        holder.tv_date.setText(converter.convertString(holder.contextView.getContext(),event.getCreated()));
        holder.tv_event_date.setText(converter.convertString(holder.contextView.getContext(),event.getEventDate()));
        holder.tv_address_location.setText(event.getLocationAddress());
        holder.tv_name_location.setText(event.getLocationName());
        holder.tv_shares.setText(event.getShareCount());
        holder.tv_comments.setText(event.getCommentCount());
        holder.tv_likes.setText(event.getLikeString(holder.contextView.getContext()));
        holder.tv_text.setText(event.getText());
        holder.tv_event_member.setText(event.getEventMember());
        holder.tv_username.setText(event.getUsername());
        holder.tv_title.setText(event.getTitle());

        holder.contextView.setOnClickListener(new OnEventClicked(holder, event));
        holder.tv_comments.setOnClickListener(new OnEventClicked(holder,event));
        holder.tv_likes.setOnClickListener(new OnEventClicked(holder,event));
        holder.tv_shares.setOnClickListener(new OnEventClicked(holder,event));

        Bitmap eventPicture = BitmapUtils.getBitmapFromString(event.getEventPicture());
        Bitmap picture = BitmapUtils.getBitmapFromString(event.getPicture());
        if(picture!=null){
            holder.iv_picture.setImageBitmap(picture);
        }
        if(eventPicture!=null){
            holder.iv_event_picture.setVisibility(View.VISIBLE);
            holder.iv_event_picture.setImageBitmap(eventPicture);
        }else{
            holder.iv_event_picture.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_picture, iv_event_picture, iv_event_picture_overlay;
        private TextView tv_username, tv_text, tv_likes, tv_comments, tv_shares, tv_date, tv_title, tv_event_date, tv_address_location,tv_name_location, tv_event_member;
        private final View contextView;
        private LinearLayout event_buttons;
        private LinearLayout event_date_img;



        private LinearLayout event_member_img;
        private LinearLayout event_location_img;
        private RelativeLayout event_layout;



        public EventViewHolder(View view) {
            super(view);
            contextView = view;
            iv_picture = (ImageView) view.findViewById(R.id.event_picture);
            iv_event_picture = (ImageView) view.findViewById(R.id.event_event_picture);

            tv_username = (TextView) view.findViewById(R.id.event_username);
            tv_text = (TextView) view.findViewById(R.id.event_text);
            tv_likes = (TextView) view.findViewById(R.id.event_like_count);
            tv_comments = (TextView) view.findViewById(R.id.event_comment_count);
            tv_event_date = (TextView) view.findViewById(R.id.event_date_tv);
            tv_address_location = (TextView) view.findViewById(R.id.event_location_address_tv);
            tv_name_location = (TextView) view.findViewById(R.id.event_location_name_tv);
            tv_shares = (TextView) view.findViewById(R.id.event_share_count);
            tv_date = (TextView) view.findViewById(R.id.event_date);
            tv_title = (TextView) view.findViewById(R.id.event_title);
            event_buttons = (LinearLayout) view.findViewById(R.id.event_option_container);
            event_date_img = (LinearLayout) view.findViewById(R.id.event_date_container);
            event_member_img = (LinearLayout) view.findViewById(R.id.event_member_container);
            event_location_img = (LinearLayout) view.findViewById(R.id.event_location_container);
            tv_event_member =(TextView) view.findViewById(R.id.event_member_tv );
            event_layout = (RelativeLayout) view.findViewById(R.id.event_relative_layout);
        }

        public ImageView getIv_picture() {
            return iv_picture;
        }

        public ImageView getIv_event_picture() {
            return iv_event_picture;
        }

        public TextView getTv_username() {
            return tv_username;
        }

        public TextView getTv_text() {
            return tv_text;
        }

        public TextView getTv_likes() {
            return tv_likes;
        }

        public TextView getTv_comments() {
            return tv_comments;
        }

        public TextView getTv_shares() {
            return tv_shares;
        }

        public TextView getTv_date() {
            return tv_date;
        }

        public TextView getTv_title() {
            return tv_title;
        }

        public View getContextView() {
            return contextView;
        }

        public LinearLayout getEvent_buttons() {
            return event_buttons;
        }

        public LinearLayout getEvent_date_img() {
            return event_date_img;
        }

        public LinearLayout getEvent_member_img() {
            return event_member_img;
        }

        public LinearLayout getEvent_location_img() {
            return event_location_img;
        }

        public ImageView getIv_event_picture_overlay() {
            return iv_event_picture_overlay;
        }

        public RelativeLayout getEvent_layout() {
            return event_layout;
        }

        public TextView getTv_event_date() {
            return tv_event_date;
        }

        public TextView getTv_address_location() {
            return tv_address_location;
        }

        public TextView getTv_name_location() {
            return tv_name_location;
        }

        public TextView getTv_event_member() {
            return tv_event_member;
        }
    }

    private class OnEventClicked implements View.OnClickListener{
        final Event event;
        final EventViewHolder holder;
        private OnEventClicked(final EventViewHolder holder, final Event event){
            this.holder = holder;
            this.event = event;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.event_like_count:
                    for (OnEventClickedListener onEventClickedListener: onEventClickedListeners){
                        onEventClickedListener.onEventLike(holder,event);
                    }
                    break;
                case R.id.participate:
                    for (OnEventClickedListener onEventClickedListener: onEventClickedListeners){
                        onEventClickedListener.onParticipateEvent(holder,event);
                    }
                    break;
                case R.id.event_share_count:
                    for (OnEventClickedListener onEventClickedListener: onEventClickedListeners){
                        onEventClickedListener.onEventShare(holder,event);
                    }
                    break;
                case R.id.event_comment_count:
                    for (OnEventClickedListener onEventClickedListener: onEventClickedListeners){
                        onEventClickedListener.onEventComment(holder,event);
                    }
                    break;
                default:
                    for (OnEventClickedListener onEventClickedListener: onEventClickedListeners){onEventClickedListener.onEventClicked(holder,event);
                    }
                    break;
            }
        }
    }

    public void addOnEventClickedListener(EventAdapter.OnEventClickedListener onEventClickedListener){
        this.onEventClickedListeners.add(onEventClickedListener);
    }

    public void removeOnEventClickedListener(EventAdapter.OnEventClickedListener onEventClickedListener){
        this.onEventClickedListeners.remove(onEventClickedListener);
    }
}
