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
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.LikeResult;
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
    public void onEventLike(final EventAdapter.EventViewHolder holder, final Event e) {

        User user = getLocalUser();
        eventService.likeEvent(user.getId(), e.getEvent_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                e.setLiked(result.isLiked());
                e.setLikeCount(result.getLikeCount());
                holder.getTv_likes().setText(e.getLikeString(getContext()));
            }

            @Override
            public void onFailure(String errorCode) {
                printError(getView(), getView().findViewById(getView().getId()), errorCode);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onEventShare(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public void onEventComment(EventAdapter.EventViewHolder holder, Event e) {

    }

    @Override
    public EventAdapter getAdapter(List<Event> events) {
        EventAdapter adapter = new EventAdapter(events);
        adapter.addOnEventClickedListener(this);
        return adapter;
    }

    @Override
    public void onSetActive(boolean flag) {
        Log.e("EventFragment", "Activeated");
        menuManager.setMenuFragment(flag?eventMenuFragment:null);
    }
}
