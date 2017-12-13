package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class ExcerciseNode extends AbstractNode {

    String name;

    private String setType;

    public ExcerciseNode(String name, String setType) {
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
}