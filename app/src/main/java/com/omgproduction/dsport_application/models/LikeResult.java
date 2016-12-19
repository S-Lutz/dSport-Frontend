package com.omgproduction.dsport_application.models;

/**
 * Created by Florian on 19.12.2016.
 */

public class LikeResult {
    private final boolean liked;
    private final String likeCount;

    public LikeResult(boolean liked, String likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getLikeCount() {
        return likeCount;
    }
}
