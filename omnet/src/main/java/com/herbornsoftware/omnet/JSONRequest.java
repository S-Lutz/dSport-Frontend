package com.herbornsoftware.omnet;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Florian on 15.03.2017.
 */

public class JSONRequest extends AsyncTask<Void, String, Void>{

    private static final String TAG = JSONRequest.class.getSimpleName();
    private URL url;
    private boolean https = false;
    private boolean doOutput = false;
    private HashMap<String, String> params;
    private RequestMethod requestMethod;
    private boolean customMethod = false;
    private boolean debug;
    private OnResultListener onResultListener;

    public interface OnResultListener{
        void onResult(JSONResponse response);
    }


    public JSONRequest(URL url) {
        this.url = url;
        this.params = new HashMap<>();
        this.requestMethod = RequestMethod.GET;
    }

    public JSONRequest(String url) throws MalformedURLException {
        this(new URL(url));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(isHttps()){

        }else {
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(10000);
                con.setConnectTimeout(15000);
                con.setRequestMethod(requestMethod.name());
                con.setDoInput(true);
                con.setDoOutput(true);
                if(isDoOutput()){
                    con.setChunkedStreamingMode(0);
                    OutputStream os = con.getOutputStream();
                    writeStream(os);
                    con.connect();
                }
                log("connection",con.toString());

                JSONResponse response = null;
                try {
                    response = new JSONResponse(con.getResponseMessage(), con.getResponseCode(), null);
                }catch (SocketTimeoutException e){
                    response = new JSONResponse("Timeout",HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);
                }

                if(con.getResponseCode()==HttpURLConnection.HTTP_OK){

                    BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                    response = new JSONResponse(con.getResponseMessage(), con.getResponseCode(), new JSONObject(readStream(in)));
                    log("Result Code", String.valueOf(response.getResponseCode()));
                    log("Result Message", response.getResponseMessage());


                }

                onResult(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(con!=null)
                    con.disconnect();
            }
        }
        return null;
    }

    private void onResult(final JSONResponse response) {
        if(onResultListener!=null) {
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            onResultListener.onResult(response);
                        }
                    }
            );
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        log("Post Data String", result.toString());
        return result.toString();
    }

    private void writeStream(OutputStream out) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(out, "UTF-8"));
        writer.write(getPostDataString(params));
        writer.flush();
        writer.close();
        out.close();
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        String result = "";
        while ((inputLine = reader.readLine()) != null){
            result += inputLine;
            log("Input Line", inputLine);
        }
        log("Result String", result);
        return result;
    }

    public JSONRequest execute() {
        super.execute();
        return this;
    }



    public URL getUrl() {
        return url;
    }

    public JSONRequest setUrl(URL url) {
        this.url = url;
        return this;
    }

    public boolean isHttps() {
        return https;
    }

    //public JSONRequest setHttps(boolean https) {
    //    this.https = https;
    //    return this;
    //}

    public boolean isDoOutput() {
        return doOutput;
    }

    public JSONRequest setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
        return this;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public JSONRequest setParams(HashMap<String, String> params) {
        this.params = params;
        setDoOutput(true);
        return this;
    }

    public JSONRequest addParam(String key, Object value){
        params.put(key,String.valueOf(value));
        setDoOutput(true);
        if(!customMethod) requestMethod = RequestMethod.POST;
        return this;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public JSONRequest setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        customMethod = true;
        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public JSONRequest setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    private void log(String title, String log){
        if(debug)
            Log.i(TAG, " -"+title+"-: "+log);
    }

    public OnResultListener getOnResultListener() {
        return onResultListener;
    }

    public JSONRequest setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
        return this;
    }
}
