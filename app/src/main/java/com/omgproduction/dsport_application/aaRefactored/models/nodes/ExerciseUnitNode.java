package com.omgproduction.dsport_application.aaRefactored.models.nodes;

import com.omgproduction.dsport_application.models.ExerciseUnit;

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