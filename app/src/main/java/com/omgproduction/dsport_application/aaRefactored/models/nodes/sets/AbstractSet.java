package com.omgproduction.dsport_application.aaRefactored.models.nodes.sets;

import com.omgproduction.dsport_application.aaRefactored.models.nodes.AbstractNode;

public class AbstractSet extends AbstractNode {
    private String time;

    public AbstractSet(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}