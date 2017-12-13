package com.omgproduction.dsport_application.aaRefactored.models.nodes.sets;

public class DistanceBasedSet extends AbstractSet {

    private String distance;

    public DistanceBasedSet(String time, String distance) {
        super(time);
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}