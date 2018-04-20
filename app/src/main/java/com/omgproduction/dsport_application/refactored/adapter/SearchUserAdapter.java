package com.omgproduction.dsport_application.refactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.interfaces.onItemClickListener;
import com.omgproduction.dsport_application.refactored.models.resultnodes.UserResultNode;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserItemViewHolder>{

    private ArrayList<UserResultNode> users;
    private Context context;
    private onItemClickListener<UserResultNode> itemClickListener;

    public enum relationshipType {FRIEND, REQUESTED, ACCEPT, ADD, DELETE, UNKNOWN}

    public class UserItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private relationshipType status = relationshipType.UNKNOWN;
        private TextView itemLabel;
        private ImageView itemPic;
        private ImageView addFriendBtn;

        UserItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.item_label);
            itemPic = (ImageView) itemView.findViewById(R.id.item_pic);
            addFriendBtn = (ImageView) itemView.findViewById(R.id.person_add);

            addFriendBtn.setOnClickListener(this);
            itemLabel.setOnClickListener(this);
            itemPic.setOnClickListener(this);
        }

        ImageView getItemPic() {
            return itemPic;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, users.get(getAdapterPosition()), status, getAdapterPosition());
        }
    }

    public SearchUserAdapter(Context context, ArrayList<UserResultNode> users, onItemClickListener<UserResultNode> itemClickListener)  {
        this.context = context;
        this.users = users;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SearchUserAdapter.UserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_row_layout_user, parent, false);
        UserItemViewHolder vh = new UserItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, int position) {
        UserResultNode userNode =  users.get(position);
        ImageView addBtn = holder.addFriendBtn;

        holder.itemLabel.setText(userNode.getUsername());
        if (userNode.getPicture() != null) {
            GlideApp
                    .with(context)
                    .load(userNode.getPicture())
                    .fitCenter()
                    .into(holder.getItemPic());
        }

        if (userNode.getFriend()) {
            addBtn.setImageResource(R.drawable.ic_logout);
        } else if (userNode.getHasRequest()) {
            addBtn.setImageResource(R.drawable.ic_friendship_received);
        } else if (userNode.getRequested()) {
            addBtn.setImageResource(R.drawable.ic_friend_request_send);
        }else {
            addBtn.setImageResource(R.drawable.ic_person_add);
        }

        setStatus(holder, userNode);
    }

    public void updateImageRessource(int adapterPosition, relationshipType updatedRelationshiptype, UserResultNode userResultNode) {
        switch (updatedRelationshiptype){
            case REQUESTED:
                userResultNode.setFriend(false);
                userResultNode.setHasRequest(false);
                userResultNode.setRequested(true);
                break;
            case FRIEND:
                userResultNode.setFriend(true);
                userResultNode.setHasRequest(false);
                userResultNode.setRequested(false);
                break;
            case DELETE:
                userResultNode.setFriend(false);
                userResultNode.setHasRequest(false);
                userResultNode.setRequested(false);
                break;
        }

        users.set(adapterPosition, userResultNode);
        notifyItemChanged(adapterPosition);
        notifyDataSetChanged();
    }

    private void setStatus(UserItemViewHolder holder, UserResultNode user) {

        if (user.getFriend()) {
            holder.status = relationshipType.FRIEND;
        } else if (user.getHasRequest()) {
            holder.status = relationshipType.ACCEPT;
        } else if (user.getRequested()) {
            holder.status = relationshipType.REQUESTED;
        }else {
            holder.status = relationshipType.ADD;
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}