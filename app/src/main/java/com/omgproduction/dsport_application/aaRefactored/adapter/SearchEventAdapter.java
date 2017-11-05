package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;

import java.util.ArrayList;

public class SearchEventAdapter extends RecyclerView.Adapter<SearchEventAdapter.EventItemViewHolder> {

    private ArrayList<EventNode> events;
    private Context context;

    public class EventItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel;
        private ImageView itemPic;

        public EventItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.item_label);
            itemPic = (ImageView) itemView.findViewById(R.id.item_pic);
        }
    }

    public SearchEventAdapter(Context context, ArrayList<EventNode> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public SearchEventAdapter.EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_row_layout_event, parent, false);
        SearchEventAdapter.EventItemViewHolder vh = new SearchEventAdapter.EventItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SearchEventAdapter.EventItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}