package com.omgproduction.dsport_application.fragments.users;

import android.graphics.ImageFormat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragment;
import com.omgproduction.dsport_application.utils.BitmapUtils;

/**
 * Created by Florian on 22.12.2016.
 */

public class UserFragment extends AbstractFragment {

    private String userId;

    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.layout_fragment_friend, container, false);
        setRefresher((SwipeRefreshLayout) view.findViewById(R.id.friend_refresher));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update();
    }

    @Override
    public void onRefresh() {
        update();
    }

    private void update() {
        UserService userService = new UserService(getContext());
        userService.getUser(userId, new RequestFuture<User>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User result) {
                updateUserValues(result);
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                printError(getView(), getView(), errorMessage);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void updateUserValues(User result) {
        ((ImageView)getView().findViewById(R.id.friend_profile_pic)).setImageBitmap(BitmapUtils.getBitmapFromString(result.getPicture()));
        ((TextView)getView().findViewById(R.id.friend_email_text)).setText(result.getEmail());
        ((TextView)getView().findViewById(R.id.friend_firstname_text)).setText(result.getFirstname());
        ((TextView)getView().findViewById(R.id.friend_lastname_text)).setText(result.getLastname());
        ((TextView)getView().findViewById(R.id.friend_username_text)).setText(result.getUsername());
    }

    public void setUser(SearchUser user) {
        this.userId = user.getId();
    }
}