package com.omgproduction.dsport_application.models;

/**
 * Created by Florian on 19.12.2016.
 */

public class ParticipateResult {
    private final boolean participating;
    private final String membercount;

    public ParticipateResult(boolean participating, String membercount) {
        this.participating = participating;
        this.membercount = membercount;
    }

    public boolean isParticipating() {
        return participating;
    }

    public String getMembercount() {
        return membercount;
    }
}
