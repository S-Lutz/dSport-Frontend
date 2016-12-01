package com.omgproduction.dsport_application.utils;

import com.omgproduction.dsport_application.config.ApplicationKeys;
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
                jsonUser.getString(ApplicationKeys.USER_ID),
                jsonUser.getString(ApplicationKeys.USERNAME),
                jsonUser.getString(ApplicationKeys.EMAIL),
                jsonUser.getString(ApplicationKeys.PICTURE),
                jsonUser.getString(ApplicationKeys.FIRSTNAME),
                jsonUser.getString(ApplicationKeys.LASTNAME),
                jsonUser.getString(ApplicationKeys.CREATED),
                jsonUser.getString(ApplicationKeys.AGB_VERSION)
        );
        return user;
    }

    public static Post convertPost(JSONObject jsonPost) throws JSONException {
        Post post = new Post(
                jsonPost.getString(ApplicationKeys.POST_ID),
                jsonPost.getString(ApplicationKeys.USERNAME),
                jsonPost.getString(ApplicationKeys.USER_ID),
                jsonPost.getString(ApplicationKeys.TITLE),
                jsonPost.getString(ApplicationKeys.PICTURE),
                jsonPost.getString(ApplicationKeys.POST_PICTURE),
                jsonPost.getString(ApplicationKeys.TEXT),
                jsonPost.getString(ApplicationKeys.CREATED),
                jsonPost.getString(ApplicationKeys.LIKE_COUNT),
                jsonPost.getString(ApplicationKeys.COMMENT_COUNT),
                jsonPost.getString(ApplicationKeys.SHARE_COUNT)
        );
        return post;
    }
}
