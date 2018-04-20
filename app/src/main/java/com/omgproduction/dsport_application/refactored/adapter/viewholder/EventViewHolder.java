package com.omgproduction.dsport_application.refactored.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    onSocialItemClickedListener itemClickListener;

    private ImageView eventProfilePic;
    private AppCompatTextView eventProfileheaderText;
    private AppCompatTextView eventProfileheaderSubText;
    private ImageView eventPicture;
    private AppCompatTextView eventTitle;
    private ImageView eventLikebtn;
    private ImageView eventCommentBtn;
    private AppCompatTextView eventDate;
    private AppCompatTextView eventLocationName;
    private AppCompatTextView eventLocationAdress;
    private AppCompatTextView eventText;
    private AppCompatTextView eventlikeCount;
    private AppCompatTextView eventCommentCount;
    private ImageView participatebtn;
    private AppCompatTextView eventParticipateCount;

    public EventViewHolder(View itemView, onSocialItemClickedListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;

        this.eventProfilePic = (ImageView) itemView.findViewById(R.id.event_profile_pic);
        this.eventProfileheaderText = (AppCompatTextView) itemView.findViewById(R.id.event_profile_header_text);
        this.eventProfileheaderSubText = (AppCompatTextView) itemView.findViewById(R.id.event_profile_header_sub_text);
        this.eventPicture = (ImageView) itemView.findViewById(R.id.new_event_picture);
        this.eventTitle = (AppCompatTextView) itemView.findViewById(R.id.event_title);
        this.eventLikebtn = (ImageView) itemView.findViewById(R.id.event_like_btn);
        this.eventCommentBtn = (ImageView) itemView.findViewById(R.id.event_comment_button);
        this.participatebtn = (ImageView) itemView.findViewById(R.id.event_participate_btn);
        this.eventParticipateCount = (AppCompatTextView) itemView.findViewById(R.id.event_participate_count);
        this.eventDate = (AppCompatTextView) itemView.findViewById(R.id.event_date);
        this.eventLocationName = (AppCompatTextView) itemView.findViewById(R.id.event_location_name);
        this.eventLocationAdress = (AppCompatTextView) itemView.findViewById(R.id.event_location_address);
        this.eventText = (AppCompatTextView) itemView.findViewById(R.id.new_event_text);
        this.eventlikeCount = (AppCompatTextView) itemView.findViewById(R.id.event_like_count);
        this.eventCommentCount = (AppCompatTextView) itemView.findViewById(R.id.event_comment_count);

        eventCommentBtn.setOnClickListener(this);
        eventLikebtn.setOnClickListener(this);
        participatebtn.setOnClickListener(this);
    }

    public ImageView getEventProfilePic() {
        return eventProfilePic;
    }

    public void setEventProfilePic(ImageView eventProfilePic) {
        this.eventProfilePic = eventProfilePic;
    }

    public AppCompatTextView getEventProfileheaderText() {
        return eventProfileheaderText;
    }

    public void setEventProfileheaderText(AppCompatTextView eventProfileheaderText) {
        this.eventProfileheaderText = eventProfileheaderText;
    }

    public AppCompatTextView getEventProfileheaderSubText() {
        return eventProfileheaderSubText;
    }

    public void setEventProfileheaderSubText(AppCompatTextView eventProfileheaderSubText) {
        this.eventProfileheaderSubText = eventProfileheaderSubText;
    }

    public ImageView getEventPicture() {
        return eventPicture;
    }

    public void setEventPicture(ImageView eventPicture) {
        this.eventPicture = eventPicture;
    }

    public AppCompatTextView getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(AppCompatTextView eventTitle) {
        this.eventTitle = eventTitle;
    }

    public ImageView getEventLikebtn() {
        return eventLikebtn;
    }

    public void setEventLikebtn(ImageView eventLikebtn) {
        this.eventLikebtn = eventLikebtn;
    }

    public ImageView getEventCommentBtn() {
        return eventCommentBtn;
    }

    public void setEventCommentBtn(ImageView eventCommentBtn) {
        this.eventCommentBtn = eventCommentBtn;
    }

    public AppCompatTextView getEventDate() {
        return eventDate;
    }

    public void setEventDate(AppCompatTextView eventDate) {
        this.eventDate = eventDate;
    }

    public AppCompatTextView getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(AppCompatTextView eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public AppCompatTextView getEventLocationAdress() {
        return eventLocationAdress;
    }

    public void setEventLocationAdress(AppCompatTextView eventLocationAdress) {
        this.eventLocationAdress = eventLocationAdress;
    }

    public AppCompatTextView getEventText() {
        return eventText;
    }

    public void setEventText(AppCompatTextView eventText) {
        this.eventText = eventText;
    }

    public AppCompatTextView getEventlikeCount() {
        return eventlikeCount;
    }

    public void setEventlikeCount(AppCompatTextView eventlikeCount) {
        this.eventlikeCount = eventlikeCount;
    }

    public AppCompatTextView getEventCommentCount() {
        return eventCommentCount;
    }

    public void setEventCommentCount(AppCompatTextView eventCommentCount) {
        this.eventCommentCount = eventCommentCount;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }

    public ImageView getParticipatebtn() {
        return participatebtn;
    }

    public void setParticipatebtn(ImageView participatebtn) {
        this.participatebtn = participatebtn;
    }

    public AppCompatTextView getEventparticipateCount() {
        return eventParticipateCount;
    }

    public void setEventparticipateCount(AppCompatTextView eventparticipateCount) {
        this.eventParticipateCount = eventparticipateCount;
    }
}