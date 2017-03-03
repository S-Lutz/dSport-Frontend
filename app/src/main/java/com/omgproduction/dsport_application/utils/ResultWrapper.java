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
            return jsonObject.getString(ERROR).equals(OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <O> O  extractValue(Converter<JSONObject,O> converter){
        try {
            return converter.convert(jsonObject.getJSONObject(VALUE));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <O> List<O> extractArray(Converter<JSONObject, O> converter, String arrayName){
        try {

            List<O> resultList = new ArrayList<O>();
            JSONArray jsonArray = jsonObject.getJSONObject(VALUE).getJSONArray(arrayName);
            for(int i = 0; i< jsonArray.length(); i++){
                resultList.add(converter.convert(jsonArray.getJSONObject(i)));
            }

            return resultList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractErrorCode(){
        try {
            return jsonObject.getString(VALUE);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractNotificationCode(){
        try {
            return jsonObject.getString(NOTIFICATION);
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
