package com.omgproduction.dsport_application.refactored.models.resultnodes;

import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseUnitNode;

public class ExerciseUnitResultPair {

    private ExerciseUnitNode exerciseUnitNode;
    private ExerciseNode exerciseNode;

    public ExerciseUnitNode getExerciseUnitNode() {
        return exerciseUnitNode;
    }

    public void setExerciseUnitNode(ExerciseUnitNode exerciseUnitNode) {
        this.exerciseUnitNode = exerciseUnitNode;
    }

    public ExerciseNode getExerciseUnit() {
        return exerciseNode;
    }

    public void setExerciseUnit(ExerciseNode exerciseUnit) {
        this.exerciseNode = exerciseUnit;
    }
}