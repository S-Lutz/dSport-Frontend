package com.omgproduction.dsport_application.models;

import java.io.Serializable;

/**
 * Created by Florian on 22.03.2017.
 */

public class Exercise implements Serializable{

    private String id;
    private String title;
    private int type;

    public Exercise(String id, String title, int type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Exercise(String title, int type) {
        this("",title,type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return title;
    }
}
