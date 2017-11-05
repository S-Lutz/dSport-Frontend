package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class EventNode extends AbstractNode {

    private String title;

    private String text;

    private String date;

    private String location;

    private String picture;

    private String event_picture;


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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}