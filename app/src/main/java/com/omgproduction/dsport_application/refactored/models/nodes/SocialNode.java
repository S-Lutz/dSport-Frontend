package com.omgproduction.dsport_application.refactored.models.nodes;

import java.io.Serializable;

public class SocialNode extends AbstractNode implements Serializable {

    private String likes;

    private String comments;



    public String getLikeCount() {
        return likes;
    }

    public void setLikeCount(String likeCount) {
        this.likes = likeCount;
    }

    public String getCommentCount() {
        return comments;
    }

    public void setCommentCount(String commentCount) {
        this.comments = commentCount;
    }


    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}