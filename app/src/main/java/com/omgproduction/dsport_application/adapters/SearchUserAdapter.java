package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 01.12.2016.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {

    private List<SearchUser> searchUsers = new ArrayList<>();

    public interface OnSearchUserClicked {
        void onSearchUserClicked(SearchUser searchUser);
    }

    private final ArrayList<OnSearchUserClicked> onSearchUserClickedListeners = new ArrayList<>();

    public SearchUserAdapter(List<SearchUser> searchUsers) {
        this.searchUsers = searchUsers;
    }

    @Override
    public SearchUserAdapter.SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_layout, parent, false);
        SearchUserAdapter.SearchUserHolder viewHolder = new SearchUserAdapter.SearchUserHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.SearchUserHolder holder, int position) {
        SearchUser searchUser = searchUsers.get(position);
        holder.tv_username.setText(searchUser.getUsername());
        holder.iv_userpic.setImageBitmap(BitmapUtils.getBitmapFromString(searchUser.getPicture()));
        if(searchUser.isFriend()){
            holder.iv_progress.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_star));
        }else if(searchUser.isRequest_sended()){
            holder.iv_progress.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_clock));
        }else if(searchUser.isRequest_received()){
            holder.iv_progress.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_friendship_received));
        }
        holder.tv_username.setOnClickListener(new SearchUserClicked(searchUser));
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }

    public static class SearchUserHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tv_username;
        private ImageView iv_userpic, iv_progress;
        private Context context;

        public SearchUserHolder(View view) {
            super(view);
            tv_username = (AppCompatTextView) view.findViewById(R.id.search_user_user_name);
            iv_userpic = (AppCompatImageView) view.findViewById(R.id.search_user_user_pic);
            iv_progress = (AppCompatImageView) view.findViewById(R.id.search_user_progress);
            context = view.getContext();
        }
        public TextView getTv_username() {
            return tv_username;
        }

        public ImageView getIv_userpic() {
            return iv_userpic;
        }

        public ImageView getIv_progress() {
            return iv_progress;
        }

        public Context getContext() {
            return context;
        }
    }

    private class SearchUserClicked implements View.OnClickListener {
        final SearchUser user;

        private SearchUserClicked(final SearchUser user) {
            this.user = user;
        }

        @Override
        public void onClick(View v) {
            for (OnSearchUserClicked searchUserClicked : onSearchUserClickedListeners) {
                searchUserClicked.onSearchUserClicked(user);
            }
        }
    }

    public List<SearchUser> getSearchUsers() {
        return searchUsers;
    }

    public void setSearchUsers(ArrayList<SearchUser> searchUsers) {
        this.searchUsers = searchUsers;
    }

    public void addOnSearchUserClicked(OnSearchUserClicked onSearchUserClicked) {
        this.onSearchUserClickedListeners.add(onSearchUserClicked);
    }

    public void removeOnSearchUserClicked(OnSearchUserClicked onSearchUserClicked) {
        this.onSearchUserClickedListeners.remove(onSearchUserClicked);
    }
}
