package com.omgproduction.dsport_application.holder;

import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.SearchUser;

import java.util.ArrayList;

/**
 * Created by Florian on 21.12.2016.
 */

public class SearchResultHolder {
    private ArrayList<SearchUser> users;
    private ArrayList<Event> events;

    public SearchResultHolder(ArrayList<SearchUser> users, ArrayList<Event> events) {
        this.users = users;
        this.events = events;
    }

    public ArrayList<SearchUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<SearchUser> users) {
        this.users = users;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
