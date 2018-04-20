package com.omgproduction.dsport_application.refactored.models.nodes;

import java.io.Serializable;

public class ExerciseNode extends AbstractNode implements Serializable {

    private String name;

    private String setType;

    public ExerciseNode(String name, String setType) {
        this.name = name;
        this.setType = setType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    @Override
    public String toString() {
        return name;
    }
}