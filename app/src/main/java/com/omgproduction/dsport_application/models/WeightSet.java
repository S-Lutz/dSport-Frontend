package com.omgproduction.dsport_application.models;

/**
 * Created by Florian on 22.03.2017.
 */

public class WeightSet {
    private long time;
    private int repeats;
    private float weight;

    public WeightSet(long time, int repeats, float weight) {
        this.time = time;
        this.repeats = repeats;
        this.weight = weight;
    }



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
