package com.omgproduction.dsport_application.aaRefactored.connection;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;

public class MultipartRequest<T> extends Request<T> {


    private static final String FILE_PART_NAME = "file";
    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final File mImageFile;

    private final BackendCallback<T> listener;
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final String body;
    private Map<String, String> headers;
    private Map<String, String> responseHeader;

    public MultipartRequest(String url, Class<T> clazz, File imageFile, Object body, Map<String, String> headers, final BackendCallback<T> listener) {
        super(Method.POST, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse == null || networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    listener.onFailure(new ErrorResponse(401, "An error occurred"));
                } else {
                    try {
                        listener.onFailure(new ErrorResponse(volleyError.networkResponse.statusCode, new String(volleyError.networkResponse.data,"UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        //TODO
                    }
                }
            }
        });

        this.listener = listener;
        this.mImageFile = imageFile;
        this.body = gson.toJson(body);
        this.clazz = clazz;
        this.headers = headers;

        buildMultipartEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    private void buildMultipartEntity() {
        mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType() {
        return mBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            responseHeader = response.headers;
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onSuccess(response, headers);
    }
}