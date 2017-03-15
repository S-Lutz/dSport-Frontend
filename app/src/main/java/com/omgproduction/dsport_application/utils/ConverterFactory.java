package com.omgproduction.dsport_application.utils;

import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.SearchEvent;
import com.omgproduction.dsport_application.models.SearchStudio;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class ConverterFactory implements ApplicationKeys {

    public static ResultConverter<JSONObject, User> createJsonToUserConverter() {
        return new ResultConverter<JSONObject, User>() {
            @Override
            public User convert(JSONObject input) {
                try {
                    return new User(
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_EMAIL),
                            input.getString(APPLICATION_USER_PICTURE),
                            input.getString(APPLICATION_USER_FIRSTNAME),
                            input.getString(APPLICATION_USER_LASTNAME),
                            input.getString(APPLICATION_USER_CREATED),
                            input.getString(APPLICATION_USER_AGBVERSION)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, Post> createJsonToPostConverter() {
        return new ResultConverter<JSONObject, Post>() {
            @Override
            public Post convert(JSONObject input) {
                try {
                    return new Post(
                            input.getString(APPLICATION_POST_POST_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_POST_TITLE),
                            input.getString(APPLICATION_USER_PICTURE),
                            input.getString(APPLICATION_POST_PICTURE),
                            input.getString(APPLICATION_POST_TEXT),
                            input.getString(APPLICATION_POST_CREATED),
                            input.getString(APPLICATION_POST_LIKECOUNT),
                            input.getString(APPLICATION_POST_COMMENTCOUNT),
                            input.getString(APPLICATION_POST_SHARECOUNT),
                            Integer.parseInt(input.getString(APPLICATION_POST_LIKED)) == 1
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, Event> createJsonToEventConverter() {
        return new ResultConverter<JSONObject, Event>() {
            @Override
            public Event convert(JSONObject input) {
                try {
                    return new Event(
                            input.getString(APPLICATION_EVENT_EVENT_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_EVENT_TITLE),
                            input.getString(APPLICATION_EVENT_EVENT_PICTURE),
                            input.getString(APPLICATION_EVENT_PICTURE),
                            input.getString(APPLICATION_EVENT_TEXT),
                            input.getString(APPLICATION_EVENT_CREATED),
                            input.getString(APPLICATION_EVENT_EVENT_DATE),
                            input.getString(APPLICATION_EVENT_MEMBERCOUNT),
                            input.getString(APPLICATION_EVENT_LOCATION),
                            input.getString(APPLICATION_EVENT_LIKECOUNT),
                            input.getString(APPLICATION_EVENT_COMMENTCOUNT),
                            input.getString(APPLICATION_EVENT_SHARECOUNT),
                            Integer.parseInt(input.getString(APPLICATION_EVENT_PARTICIPATING)) == 1,
                            Integer.parseInt(input.getString(APPLICATION_EVENT_LIKED)) == 1
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, Comment> createJsonToCommentConverter() {
        return new ResultConverter<JSONObject, Comment>() {
            @Override
            public Comment convert(JSONObject input) {
                try {
                    return new Comment(
                            input.getString(APPLICATION_COMMENT_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_COMMENT_PICTURE),
                            input.getString(APPLICATION_COMMENT_TEXT),
                            input.getString(APPLICATION_COMMENT_CREATED),
                            input.getString(APPLICATION_COMMENT_LIKECOUNT),
                            Integer.parseInt(input.getString(APPLICATION_COMMENT_LIKED)) == 1
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, LikeResult> createJsonToPostLikeResultConverter() {
        return new ResultConverter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_POST_LIKED)) == 1,
                            input.getString(APPLICATION_POST_LIKECOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, LikeResult> createJsonToCommentLikeResultConverter() {
        return new ResultConverter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_COMMENT_LIKED)) == 1,
                            input.getString(APPLICATION_COMMENT_LIKECOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, LikeResult> createJsonToEventLikeResultConverter() {
        return new ResultConverter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_EVENT_LIKED)) == 1,
                            input.getString(APPLICATION_EVENT_LIKECOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, Like> createJsonToLikeConverter() {
        return new ResultConverter<JSONObject, Like>() {
            @Override
            public Like convert(JSONObject input) {
                try {
                    return new Like(
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_USER_USERNAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, SearchResultHolder> createJsonToSearchResultHolderConverter(){
        return new ResultConverter<JSONObject, SearchResultHolder>() {
            @Override
            public SearchResultHolder convert(JSONObject input) {

                JSONArray userList = null;
                try {
                    userList = input.getJSONArray(APPLICATION_USERS);
                    JSONArray eventList = input.getJSONArray(APPLICATION_EVENTS);
                    JSONArray studioList = input.getJSONArray(APPLICATION_STUDIOS);

                    ResultConverter<JSONObject, SearchUser> searchUserConverter = createJsonToSearchUserConverter();
                    ResultConverter<JSONObject, SearchEvent> searchEventConverter = createJsonToSearchEventConverter();
                    ResultConverter<JSONObject, SearchStudio> searchStudioConverter = createJsonToSearchStudioConverter();

                    ArrayList<SearchUser> users = new ArrayList<>();
                    for (int i = 0; i < userList.length(); i++) {
                        users.add(searchUserConverter.convert(userList.getJSONObject(i)));
                    }
                    ArrayList<SearchEvent> events = new ArrayList<>();
                    for (int i = 0; i < eventList.length(); i++) {
                        events.add(searchEventConverter.convert(eventList.getJSONObject(i)));
                    }
                    ArrayList<SearchStudio> studios = new ArrayList<>();
                    for (int i = 0; i < studioList.length(); i++) {
                        studios.add(searchStudioConverter.convert(studioList.getJSONObject(i)));
                    }

                    return new SearchResultHolder(users, events, studios);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }


    public static ResultConverter<JSONObject, SearchUser> createJsonToSearchUserConverter(){
        return new ResultConverter<JSONObject, SearchUser>() {
            @Override
            public SearchUser convert(JSONObject input) {
                try {
                    return new SearchUser(
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_PICTURE),
                            input.getString(APPLICATION_USER_FIRSTNAME),
                            input.getString(APPLICATION_USER_LASTNAME),
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_FRIEND_FRIEND)) == 1,
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_FRIEND_REQUEST_RECEIVED)) == 1,
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_FRIEND_REQUEST_SENDED)) == 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, SearchStudio> createJsonToSearchStudioConverter(){
        return new ResultConverter<JSONObject, SearchStudio>() {
            @Override
            public SearchStudio convert(JSONObject input) {
                return new SearchStudio();
            }
        };
    }

    public static ResultConverter<JSONObject, SearchEvent> createJsonToSearchEventConverter(){
        return new ResultConverter<JSONObject, SearchEvent>() {
            @Override
            public SearchEvent convert(JSONObject input) {
                return new SearchEvent();
            }
        };
    }
}