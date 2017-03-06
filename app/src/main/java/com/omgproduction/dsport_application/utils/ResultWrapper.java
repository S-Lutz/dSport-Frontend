package com.omgproduction.dsport_application.utils;

import android.content.Context;

import com.omgproduction.dsport_application.config.ResultKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 21.10.2016.
 */
public class ResultWrapper implements ResultKeys{

    private JSONObject jsonObject;
    private Context context;

    public ResultWrapper(Context context, JSONObject jsonObject){
        this.context = context;
        this.jsonObject = jsonObject;
    }

    public boolean isOk(){
        try {
            return jsonObject.getString(RESULT_ERROR_KEY).equals(RESULT_OK_VALUE);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <O> O  extractValue(Converter<JSONObject,O> converter){
        try {
            return converter.convert(jsonObject.getJSONObject(RESULT_VALUE_VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject extractValue(){
        try {
                return jsonObject.getJSONObject(RESULT_VALUE_VALUE);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
        }
    }

    public String extractString(String name){
        try {
            return jsonObject.getJSONObject(RESULT_VALUE_VALUE).getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    public <O> List<O> extractArray(Converter<JSONObject, O> converter, String arrayName){
        try {

            List<O> resultList = new ArrayList<O>();
            JSONArray jsonArray = jsonObject.getJSONObject(RESULT_VALUE_VALUE).getJSONArray(arrayName);
            for(int i = 0; i< jsonArray.length(); i++){
                O item = converter.convert(jsonArray.getJSONObject(i));
                if(item!=null){
                    resultList.add(item);
                }
            }

            return resultList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractErrorCode(){
        try {
            return jsonObject.getString(RESULT_VALUE_VALUE);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractNotificationCode(){
        try {
            return jsonObject.getString(RESULT_NOTIFICATION_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractErrorString(){
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(extractErrorCode(), "string", packageName);
        return context.getString(resId);
    }

}
