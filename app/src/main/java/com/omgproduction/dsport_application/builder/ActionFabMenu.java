package com.omgproduction.dsport_application.builder;

import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Florian on 20.11.2016.
 */

public class ActionFabMenu {
    private FloatingActionButton rootFab;
    private ArrayList<ArrayList<FloatingActionButton>> states = new ArrayList<>();
    private ArrayList<FloatingActionButton> currentState = new ArrayList<>();

    public ActionFabMenu(FloatingActionButton rootFab){
        this.rootFab = rootFab;
    }

    public ActionFabMenu addSubFab(FloatingActionButton subFab, int state){
        if(states.get(state)==null){
            states.add(state,new ArrayList<FloatingActionButton>());
        }
        states.get(state).add(subFab);
        return this;
    }

    public void switchState(int state){
        currentState = states.get(state);
    }

    public void build(){
        for(ArrayList<FloatingActionButton> s : states){
            if(s!=null){
                int menuSize = s.size();

            }

        }
    }

}
