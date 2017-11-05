package com.omgproduction.dsport_application.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class JSONHttpsRequest extends AsyncTask<Void, Void, JSONResponse> {


    private URL url;
    private String method;
    private HashMap<String, String> params;
    //private JSONHttpsRequest.OnResultListener onResultListener;

    //interface OnResultListener{
    //    void onResult(JSONResponse response);
    //}

    private JSONHttpsRequest(URL url) {
        this.url = url;
        this.params = new HashMap<>();
    }

    public JSONHttpsRequest(String url) throws MalformedURLException {
        this(new URL(url));
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("onPreExecute", "onPreExecute");
    }

    @Override
    protected JSONResponse doInBackground(Void... voids) {

        Log.e("doInBackground", "doInBackground");

        JSONResponse response = null;

        try{
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(this.method);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject(params);

            Log.i("Appygram Example","Sending: "+json);

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            try {
                response = new JSONResponse(conn.getResponseMessage(), conn.getResponseCode(), null);
            }catch (SocketTimeoutException e){
                response = new JSONResponse("Timeout", HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);
            }


            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){

                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                response = new JSONResponse(conn.getResponseMessage(), conn.getResponseCode(), new JSONObject(convertStreamToString(in)));
            }

        } catch (IOException x) {
            Log.e("Appygram Example","Error sending appygram", x);

        } catch (JSONException e) {

        }

        return response;
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(JSONResponse result) {
        super.onPostExecute(result);
        Log.e("onPostExecute", "onPostExecute" + result);
    }

    public JSONHttpsRequest addParam(String key, Object value){
        params.put(key,String.valueOf(value));
        return this;
    }

    public JSONHttpsRequest setHttpMethod(String method){
        this.method = method;
        return this;
    }

    //private void onResult(final JSONResponse response) {
    //    if(onResultListener!=null) {
    //        BackendModels Handler(Looper.getMainLooper()).post(
    //                BackendModels Runnable() {
    //                    @Override
    //                    public void run() {
    //                        onResultListener.onResult(response);
    //                    }
    //                }
    //        );
    //    }
    //}

    //public JSONHttpsRequest execute() {
    //    super.execute();
    //    return this;
    //}

    //public JSONHttpsRequest setOnResultListener(JSONHttpsRequest.OnResultListener onResultListener) {
    //    this.onResultListener = onResultListener;
    //    return this;
    //}

}