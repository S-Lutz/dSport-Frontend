package com.omgproduction.dsport_application.aaRefactored.models.nodes.sets;

public class RepeatBasedSet extends AbstractSet {

    private String weight;
    private String repeats;

    public RepeatBasedSet(String time, String weight, String repeats) {
        super(time);
        this.weight = weight;
        this.repeats = repeats;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRepeats() {
        return repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }
}