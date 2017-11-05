package com.omgproduction.dsport_application.utils;

import android.util.Log;

import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.DistanceExerciseUnit;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.Exercise;
import com.omgproduction.dsport_application.models.ExerciseUnit;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Participate;
import com.omgproduction.dsport_application.models.ParticipateResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.TimedExerciseUnit;
import com.omgproduction.dsport_application.models.TimedSet;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.models.WeightExerciseUnit;
import com.omgproduction.dsport_application.models.WeightSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class ConverterFactory implements ApplicationKeys {

    public static ResultConverter<JSONObject, User> createJsonToUserConverter() {
      // return new ResultConverter<JSONObject, User>() {
      //     @Override
      //     public User convert(JSONObject input) {
      //         try {
      //             return new User(
      //                     input.getString(APPLICATION_USER_USER_ID),
      //                     input.getString(APPLICATION_USER_USERNAME),
      //                     input.getString(APPLICATION_USER_EMAIL),
      //                     input.getString(APPLICATION_USER_PICTURE),
      //                     input.getString(APPLICATION_USER_FIRSTNAME),
      //                     input.getString(APPLICATION_USER_LASTNAME),
      //                     input.getString(APPLICATION_USER_CREATED),
      //                     input.getString(APPLICATION_USER_AGBVERSION)
      //             );
      //         } catch (JSONException e) {
      //             e.printStackTrace();
      //             return null;
      //         }
      //     }
      // };
        return null;
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

    public static ResultConverter<JSONObject, ParticipateResult> createJsonToEventParticipateResultConverter() {
        return new ResultConverter<JSONObject, ParticipateResult>() {
            @Override
            public ParticipateResult convert(JSONObject input) {
                try {
                    return new ParticipateResult(
                            Integer.parseInt(input.getString(APPLICATION_EVENT_PARTICIPATING)) == 1,
                            input.getString(APPLICATION_EVENT_MEMBERCOUNT));
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

    public static ResultConverter<JSONObject, LikeResult> createJsonToExerciseUnitLikeResultConverter() {
        return new ResultConverter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKED)) == 1,
                            input.getString(APPLICATION_EXERCISE_UNIT_LIKECOUNT));
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

    public static ResultConverter<JSONObject, Participate> createJsonToParticipateConverter() {
        return new ResultConverter<JSONObject, Participate>() {
            @Override
            public Participate convert(JSONObject input) {
                try {
                    return new Participate(
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
                    ResultConverter<JSONObject, Event> searchEventConverter = createJsonToEventConverter();

                    ArrayList<SearchUser> users = new ArrayList<>();
                    for (int i = 0; i < userList.length(); i++) {
                        users.add(searchUserConverter.convert(userList.getJSONObject(i)));
                    }
                    ArrayList<Event> events = new ArrayList<>();
                    for (int i = 0; i < eventList.length(); i++) {
                        events.add(searchEventConverter.convert(eventList.getJSONObject(i)));
                    }

                    return new SearchResultHolder(users, events);
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
                            input.getString(APPLICATION_USER_EMAIL),
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

    public static ResultConverter<JSONObject, Exercise> createJsonToExerciseConverter(){
        return new ResultConverter<JSONObject, Exercise>() {
            @Override
            public Exercise convert(JSONObject input) {
                try {
                    return new Exercise(
                            input.getString(APPLICATION_EXERCISE_ID),
                            input.getString(APPLICATION_EXERCISE_TITLE),
                            Integer.parseInt(input.getString(APPLICATION_EXERCISE_TYPE))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }


    public static ResultConverter<JSONObject, ExerciseUnit> createJsonToExerciseUnitConverter(){
        return new ResultConverter<JSONObject, ExerciseUnit>() {
            @Override
            public ExerciseUnit convert(JSONObject input) {
                try {
                    switch (Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_TYPE))){
                        case 0:

                            ResultConverter<JSONObject, WeightSet> weightSetConverter = createJsonToWeightSetConverter();

                            JSONArray setList = input.getJSONArray(APPLICATION_EXERCISE_UNIT_SETS);
                            ArrayList<WeightSet> sets = new ArrayList<>();
                            for (int i = 0; i < setList.length(); i++) {
                                sets.add(weightSetConverter.convert(setList.getJSONObject(i)));
                            }

                            return new WeightExerciseUnit(
                                    input.getString(APPLICATION_EXERCISE_UNIT_ID),
                                    input.getString(APPLICATION_USER_USER_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_EXERCISE_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_TITLE),
                                    sets,
                                    input.getString(APPLICATION_USER_PICTURE),
                                    input.getString(APPLICATION_USER_USERNAME),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKECOUNT)),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKED)) == 1,
                                    input.getString(APPLICATION_EXERCISE_UNIT_CREATED));
                        case 1:
                            return new DistanceExerciseUnit(
                                    input.getString(APPLICATION_EXERCISE_UNIT_ID),
                                    input.getString(APPLICATION_USER_USER_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_EXERCISE_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_TITLE),
                                    Long.parseLong(input.getString(APPLICATION_EXERCISE_UNIT_TIME)=="null"?"0":input.getString(APPLICATION_EXERCISE_UNIT_TIME)),
                                    Float.parseFloat(input.getString(APPLICATION_EXERCISE_DISTANCE)),
                                    input.getString(APPLICATION_USER_PICTURE),
                                    input.getString(APPLICATION_USER_USERNAME),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKECOUNT)),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKED)) == 1,
                                    input.getString(APPLICATION_EXERCISE_UNIT_CREATED));
                        case 2:

                            ResultConverter<JSONObject, TimedSet> timedSetConverter = createJsonToTimedSetConverter();

                            JSONArray setList2 = input.getJSONArray(APPLICATION_EXERCISE_UNIT_SETS);
                            ArrayList<TimedSet> sets2 = new ArrayList<>();
                            for (int i = 0; i < setList2.length(); i++) {
                                sets2.add(timedSetConverter.convert(setList2.getJSONObject(i)));
                            }
                            return new TimedExerciseUnit(
                                    input.getString(APPLICATION_EXERCISE_UNIT_ID),
                                    input.getString(APPLICATION_USER_USER_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_EXERCISE_ID),
                                    input.getString(APPLICATION_EXERCISE_UNIT_TITLE),
                                    sets2,
                                    input.getString(APPLICATION_USER_PICTURE),
                                    input.getString(APPLICATION_USER_USERNAME),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKECOUNT)),
                                    Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_LIKED)) == 1,
                                    input.getString(APPLICATION_EXERCISE_UNIT_CREATED));
                        default: return null;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, WeightSet> createJsonToWeightSetConverter(){
        return new ResultConverter<JSONObject, WeightSet>() {
            @Override
            public WeightSet convert(JSONObject input) {
                try {
                    Log.e("TIME",input.getString(APPLICATION_EXERCISE_UNIT_TIME));
                    return new WeightSet(
                            Long.parseLong(input.getString(APPLICATION_EXERCISE_UNIT_TIME)),
                            Integer.parseInt(input.getString(APPLICATION_EXERCISE_UNIT_REPEATS)),
                            Float.parseFloat(input.getString(APPLICATION_EXERCISE_UNIT_WEIGHT))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static ResultConverter<JSONObject, TimedSet> createJsonToTimedSetConverter(){
        return new ResultConverter<JSONObject, TimedSet>() {
            @Override
            public TimedSet convert(JSONObject input) {
                try {
                    return new TimedSet(
                            Long.parseLong(input.getString(APPLICATION_EXERCISE_UNIT_TIME))
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}