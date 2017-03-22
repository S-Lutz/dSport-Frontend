package com.omgproduction.dsport_application.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.FriendActivity;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 01.12.2016.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder>{

    private List<SearchUser> searchUsers = new ArrayList<>();

    public interface OnUpdateTrigger{
        void onUpdate();
    }

    private final OnUpdateTrigger onUpdateTrigger;

    public SearchUserAdapter(List<SearchUser> searchUsers, OnUpdateTrigger onUpdateTrigger) {
        this.searchUsers = searchUsers;
        this.onUpdateTrigger = onUpdateTrigger;
    }

    @Override
    public SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_layout, parent, false);
        SearchUserHolder viewHolder = new SearchUserHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserHolder holder, int position) {
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
        holder.tv_username.setOnClickListener(new SearchUserClicked(searchUser, holder.getContext()));
        holder.tv_username.setOnLongClickListener(new SearchUserLongClicked(searchUser, holder.getContext()));
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
        final Context context;

        private SearchUserClicked(final SearchUser user, final Context context) {
            this.user = user;
            this.context = context;
        }

        @Override
        public void onClick(View v) {


            if(user.isFriend()){
                Intent i = new Intent(context, FriendActivity.class);
                i.putExtra(ApplicationKeys.APPLICATION_FRIEND_FRIEND,user);
                context.startActivity(i);
            }else if(user.isRequest_received()){
                acceptFriendship(user, context);
            }else if(user.isRequest_sended()){
                alreadySended(user,context);
            }else{
                addFriend(user, context);
            }


        }
    }

    private void addFriend(final SearchUser user, final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.add_friend)
                .setMessage(context.getResources().getQuantityString(R.plurals.add_user_as_friend, 1, user.getUsername()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserService service = new UserService(context);
                        User localUser = service.getLocalUser();

                        service.sendFriendshipRequest(localUser.getId(),user.getId(),new RequestFuture<Void>(){
                            @Override
                            public void onSuccess(Void result) {
                                onUpdateTrigger.onUpdate();
                            }

                            @Override
                            public void onFailure(int errorCode, String errorMessage) {
                                Toast.makeText(context, context.getResources().getQuantityString(R.plurals.add_friend_failed, 1, user.getUsername())+" "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        declineFriendship(user, context);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void acceptFriendship(final SearchUser user,final Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.accept_friend)
                .setMessage(context.getResources().getQuantityString(R.plurals.add_user_as_friend, 1, user.getUsername()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserService service = new UserService(context);
                        User localUser = service.getLocalUser();

                        service.acceptFriendship(localUser.getId(),user.getId(),new RequestFuture<Void>(){
                            @Override
                            public void onSuccess(Void result) {
                                onUpdateTrigger.onUpdate();
                            }
                            @Override
                            public void onFailure(int errorCode, String errorMessage) {
                                Toast.makeText(context, context.getResources().getQuantityString(R.plurals.accept_friend_failed, 1, user.getUsername())+" "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        declineFriendship(user, context);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void declineFriendship(final SearchUser user, final Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.decline_friend)
                .setMessage(context.getResources().getQuantityString(R.plurals.decline_user_as_friend, 1, user.getUsername()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserService service = new UserService(context);
                        User localUser = service.getLocalUser();

                        service.declineFriendship(localUser.getId(),user.getId(),new RequestFuture<Void>(){
                            @Override
                            public void onSuccess(Void result) {
                                onUpdateTrigger.onUpdate();
                            }
                            @Override
                            public void onFailure(int errorCode, String errorMessage) {
                                Toast.makeText(context, context.getResources().getQuantityString(R.plurals.decline_friend_failed, 1, user.getUsername())+" "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteFriend(final SearchUser user, final Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_friendship)
                .setMessage(context.getResources().getQuantityString(R.plurals.delete_user_as_friend, 1, user.getUsername()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserService service = new UserService(context);
                        User localUser = service.getLocalUser();

                        service.deleteFriendship(localUser.getId(),user.getId(),new RequestFuture<Void>(){
                            @Override
                            public void onSuccess(Void result) {
                                onUpdateTrigger.onUpdate();
                            }
                            @Override
                            public void onFailure(int errorCode, String errorMessage) {
                                Toast.makeText(context, context.getResources().getQuantityString(R.plurals.delete_friend_failed, 1, user.getUsername())+" "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void alreadySended(final SearchUser user, final Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.send_request)
                .setMessage(context.getResources().getQuantityString(R.plurals.already_sended, 1, user.getUsername()))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class SearchUserLongClicked implements View.OnLongClickListener {
        final SearchUser user;
        final Context context;

        private SearchUserLongClicked(final SearchUser user, final Context context) {
            this.user = user;
            this.context = context;
        }

        @Override
        public boolean onLongClick(View v) {
            if(user.isFriend()){
                deleteFriend(user,context);
            }else if(user.isRequest_received()){
                acceptFriendship(user, context);
            }else if(user.isRequest_sended()){
                alreadySended(user,context);
            }else{
                addFriend(user,context);
            }
            return false;

        }
    }

    public List<SearchUser> getSearchUsers() {
        return searchUsers;
    }

    public void setSearchUsers(ArrayList<SearchUser> searchUsers) {
        this.searchUsers = searchUsers;
    }
}
