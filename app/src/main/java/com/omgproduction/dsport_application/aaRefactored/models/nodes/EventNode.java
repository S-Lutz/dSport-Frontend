package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class EventNode extends SocialNode {

    private String title;

    private String text;

    private String time;

    private String locationName;

    private String locationAdress;

    private String picture;

    private String event_picture;

    private Boolean participating;

    private String participates;

    public EventNode(String title, String text, String time, String locationName, String locationAdress) {
        super();
        this.title = title;
        this.text = text;
        this.time = time;
        this.locationName = locationName;
        this.locationAdress = locationAdress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return time;
    }

    public void setDate(String date) {
        this.time = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAdress() {
        return locationAdress;
    }

    public void setLocationAdress(String locationAdress) {
        this.locationAdress = locationAdress;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEvent_picture() {
        return event_picture;
    }

    public void setEvent_picture(String event_picture) {
        this.event_picture = event_picture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getParticipating() {
        return participating;
    }

    public void setParticipating(Boolean participating) {
        this.participating = participating;
    }

    public String getParticipates() {
        return participates;
    }

    public void setParticipates(String participates) {
        this.participates = participates;
    }
}