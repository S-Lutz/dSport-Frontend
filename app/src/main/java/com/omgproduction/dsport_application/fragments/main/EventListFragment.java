package com.omgproduction.dsport_application.fragments.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.EventAdapter;
import com.omgproduction.dsport_application.fragments.helper.EventMenuFragment;
import com.omgproduction.dsport_application.fragments.helper.UniversalListFragment;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.EventService;

import java.util.List;

public class EventListFragment extends UniversalListFragment<Event, EventAdapter> implements EventAdapter.OnEventClickedListener{

    private EventService eventService;
    private EventMenuFragment eventMenuFragment;

    public EventListFragment() {
        eventService = new EventService(getContext());
        eventMenuFragment = new EventMenuFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_fragment_event, container, false);
        setRefresher((SwipeRefreshLayout) view.findViewById(R.id.event_refresher));
        update();

        return view;
    }

    @Override
    protected void updatePrivate() {
        User user = getLocalUser();
        eventService.getEventBoard(user.getId(), user.getId(), EventListFragment.this);
    }


    @Override
    protected void updateGlobal() {
        User user = getLocalUser();
        //TODO NOTIFICATION
        eventService.getAllEvents(user.getId(), EventListFragment.this);
    }

    @Override
    public void onEventClicked(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public void onEventLike(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public void onEventShare(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public void onEventComment(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public EventAdapter getAdapter(List<Event> events) {
        return new EventAdapter(events);
    }

    @Override
    public void onSetActive(boolean flag) {
        Log.e("EventFragment", "Activeated");
        menuManager.setMenuFragment(flag?eventMenuFragment:null);
    }
}
