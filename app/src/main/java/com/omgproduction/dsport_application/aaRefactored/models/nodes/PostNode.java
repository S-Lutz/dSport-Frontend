package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class PostNode extends SocialNode {

    private String text;

    private String title;

    private String picture;


    public PostNode(String title, String text) {
        super();
        this.title = title;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}