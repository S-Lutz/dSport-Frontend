package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class CommentNode extends SocialNode {

    private String text;

    private String picture;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}