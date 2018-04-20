package com.omgproduction.dsport_application.refactored.models.nodes;



import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PostNode extends SocialNode {

    @NotNull
    private String text;

    @NotNull
    private String title;

    @Nullable
    private String picture;


    public PostNode(@NotNull String title, @NotNull String text) {
        this(title, text, null);
    }

    public PostNode(@NotNull String title, @NotNull String text,@Nullable String picture) {
        super();
        this.title = title;
        this.text = text;
        this.picture = picture;
    }


    @NotNull
    public String getText() {
        return text;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Nullable
    public String getPicture() {
        return picture;
    }

    public void setPicture(@Nullable String picture) {
        this.picture = picture;
    }

}


