package com.herbornsoftware.omnet;

import org.json.JSONObject;

import java.net.HttpURLConnection;

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

    public <T> T convert(ResultConverter<JSONObject, T> converter){
        return converter.convert(jsonObject);
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
}
