package com.omgproduction.dsport_application.holder;

import com.omgproduction.dsport_application.models.SearchEvent;
import com.omgproduction.dsport_application.models.SearchStudio;
import com.omgproduction.dsport_application.models.SearchUser;

import java.util.ArrayList;

/**
 * Created by Florian on 21.12.2016.
 */

public class SearchResultHolder {
    private ArrayList<SearchUser> users;
    private ArrayList<SearchEvent> events;
    private ArrayList<SearchStudio> studios;

    public SearchResultHolder(ArrayList<SearchUser> users, ArrayList<SearchEvent> events, ArrayList<SearchStudio> studios) {
        this.users = users;
        this.events = events;
        this.studios = studios;
    }

    public ArrayList<SearchUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<SearchUser> users) {
        this.users = users;
    }

    public ArrayList<SearchEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<SearchEvent> events) {
        this.events = events;
    }

    public ArrayList<SearchStudio> getStudios() {
        return studios;
    }

    public void setStudios(ArrayList<SearchStudio> studios) {
        this.studios = studios;
    }
}
