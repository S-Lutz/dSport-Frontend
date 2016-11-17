package com.omgproduction.dsport_application.utils;

import android.util.Log;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.controller.ApplicationController;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class Converter {
    public static User convertUser(JSONObject jsonUser) throws JSONException {
        User user = new User(
                jsonUser.getString(Keys.USER_ID),
                jsonUser.getString(Keys.USERNAME),
                jsonUser.getString(Keys.EMAIL),
                jsonUser.getString(Keys.PICTURE),
                jsonUser.getString(Keys.FIRSTNAME),
                jsonUser.getString(Keys.LASTNAME),
                jsonUser.getString(Keys.CREATED),
                jsonUser.getString(Keys.AGB_VERSION)
        );
        return user;
    }

    public static Post convertPost(JSONObject jsonPost) throws JSONException {
        Post post = new Post(
                jsonPost.getString(Keys.POST_ID),
                jsonPost.getString(Keys.USERNAME),
                jsonPost.getString(Keys.USER_ID),
                jsonPost.getString(Keys.TITLE),
                jsonPost.getString(Keys.PICTURE),
                jsonPost.getString(Keys.POST_PICTURE),
                jsonPost.getString(Keys.TEXT),
                jsonPost.getString(Keys.CREATED),
                jsonPost.getString(Keys.LIKE_COUNT),
                jsonPost.getString(Keys.COMMENT_COUNT),
                jsonPost.getString(Keys.SHARE_COUNT)
        );
        return post;
    }
}
