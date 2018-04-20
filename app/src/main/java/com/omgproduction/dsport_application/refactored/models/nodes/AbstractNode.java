package com.omgproduction.dsport_application.refactored.models.nodes;

import java.io.Serializable;

public class AbstractNode implements Serializable {



    private Long id;
    private String updated;
    private String created;


    public Long getId() {
        return id;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCreated() {
        return created;
    }
}