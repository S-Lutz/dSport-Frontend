package com.omgproduction.dsport_application.refactored.models.nodes;


public class ExerciseUnitNode extends SocialNode{

    private ExerciseNode ofType;

    public ExerciseUnitNode() {
    }

    public ExerciseNode getOfType() {
        return ofType;
    }

    public void setOfType(ExerciseNode ofType) {
        this.ofType = ofType;
    }
}