package com.omgproduction.dsport_application.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 15.03.2017.
 */

public class JSONResponse{
    private final String responseMessage;
    private final int responseCode;
    private final JSONObject jsonObject;

    public JSONResponse(String responseMessage, int responseCode, JSONObject jsonObject) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.jsonObject = jsonObject;
    }
    public <Output> Output getValue(ResultConverter<JSONObject, Output> converter){
        return converter.convert(jsonObject);
    }
    public <Output> List<Output> getArray(ResultConverter<JSONObject, Output> converter, String arrayName){
        try {

            List<Output> resultList = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(arrayName);
            for(int i = 0; i< jsonArray.length(); i++){
                Output item = converter.convert(jsonArray.getJSONObject(i));
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
    public String getResponseMessage() {
        return responseMessage;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public JSONObject getJsonObject() {
        return jsonObject;
    }
    public boolean isOk(){
        return responseCode == HttpURLConnection.HTTP_OK && jsonObject!=null;
    }
    public String getString(String name){
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }
}
