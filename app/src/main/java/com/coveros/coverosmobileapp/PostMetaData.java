package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;

import static com.coveros.coverosmobileapp.PostList.getActivity;

/**
 * Created by maria on 6/16/2017.
 */

public class PostMetaData {
    String title, author, date;
    AlertDialog errorMessage;


    public PostMetaData(JsonObject postJson) throws Exception {
        title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
        getAuthor(postJson.get("id").getAsInt());
        date = postJson.get("date").getAsString();
    }

    public void getAuthor(int id) throws Exception{

        String url = "https://www.dev.secureci.com/wp-json/wp/v2/users/" + id + "&fields=name";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject authorJson = new JsonParser().parse(response).getAsJsonObject();
                author = authorJson.get("name").getAsString();
            }
        }, getErrorListener());
        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(request);
    }

    private Response.ErrorListener getErrorListener() {
        Response.ErrorListener responseListener = new Response.ErrorListener() {
            // logs error
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse errorNetworkResponse = volleyError.networkResponse;
                String errorData = "";
                try {
                    if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
                        errorData = new String(errorNetworkResponse.data, "UTF-8");
                    }
                } catch(Exception e) {
                    Log.e("Error", e.toString());
                }
                Log.e("Volley error", errorData);
            }
        };
        return responseListener;
    }

    public String getTitle() { return title; }
    public String getAuthor() {return author; }
    public String getDate() { return date; }


}

