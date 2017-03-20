package com.omgproduction.dsport_application.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.EventDetailActivity;
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
        Intent i = new Intent(getContext(), EventDetailActivity.class);
        i.putExtra(INTENT_EVENT, e);

        Pair<View, String> p1 = Pair.create((View) holder.getIv_picture(), getString(R.string.transition_event_picture));
        Pair<View, String> p2 = Pair.create((View) holder.getTv_username(), getString(R.string.transition_event_username));
        Pair<View, String> p3 = Pair.create((View) holder.getTv_title(), getString(R.string.transition_event_title));
        Pair<View, String> p4 = Pair.create((View) holder.getTv_text(), getString(R.string.transition_event_text));
        Pair<View, String> p5 = Pair.create((View) holder.getEvent_buttons(), getString(R.string.transition_event_buttons));
        Pair<View, String> p6 = Pair.create((View) holder.getTv_date(), getString(R.string.transition_event_date));
        Pair<View, String> p7 = Pair.create((View) holder.getTv_event_date(), getString(R.string.transition_event_event_date));
        Pair<View, String> p8 = Pair.create((View) holder.getIv_event_picture(), getString(R.string.transition_event_event_picture));
        Pair<View, String> p9 = Pair.create((View) holder.getTv_event_member(), getString(R.string.transition_event_member));
        Pair<View, String> p10 = Pair.create((View) holder.getTv_name_location(), getString(R.string.transition_event_location_name));
        Pair<View, String> p11 = Pair.create((View) holder.getTv_address_location(), getString(R.string.transition_event_location_adress));
        Pair<View, String> p12 = Pair.create((View) holder.getEvent_date_img(), getString(R.string.transition_event_date_image));
        Pair<View, String> p13 = Pair.create((View) holder.getEvent_member_img(), getString(R.string.transition_event_member_image));
        Pair<View, String> p14 = Pair.create((View) holder.getEvent_location_img(), getString(R.string.transition_event_location_image));

        ActivityOptionsCompat options;

        options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14);

        startActivity(i, options.toBundle());
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
            public void onFailure(int errorCode, String errorValue) {
                printError(getView(), getView().findViewById(getView().getId()), errorValue);
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
