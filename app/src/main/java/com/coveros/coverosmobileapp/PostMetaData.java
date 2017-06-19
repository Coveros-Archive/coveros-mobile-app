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

import java.text.DateFormat;

import static com.coveros.coverosmobileapp.PostList.getActivity;

/**
 * Created by maria on 6/16/2017.
 */

public class PostMetaData {
    AlertDialog errorMessage;

    String heading, subheading;
//    String author;


    public PostMetaData(JsonObject postJson) throws Exception {
        String title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
        Log.d("checking author json", Integer.toString((postJson.get("author").getAsInt())));
//        retrieveAuthor(postJson.get("author").getAsInt()); // I don't like this as a void method, but leaving for now
        String author = postJson.get("author").getAsString();
        String date = postJson.get("date").getAsString();
        heading = StringEscapeUtils.unescapeHtml4(title);
        Log.d("HEADING: ", heading);
//        subheading = StringEscapeUtils.unescapeHtml4(author + "\t" + DateFormat.getDateInstance().format(date));
        subheading = StringEscapeUtils.unescapeHtml4(author + "\t" + date);
        Log.d("SUBHEADING", subheading);

    }

//    public void retrieveAuthor(int id) throws Exception {
//
//        String url = "https://www.dev.secureci.com/wp-json/wp/v2/users/" + id;
//        Log.d("AUTHOR ID", Integer.toString(id));
//
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JsonObject authorJson = new JsonParser().parse(response).getAsJsonObject();
//                author = authorJson.get("name").getAsString();
//                Log.d("Author: ", author);
//            }
//        }, getErrorListener());
//        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        rQueue.add(request);
//    }

//    private Response.ErrorListener getErrorListener() {
//        Response.ErrorListener responseListener = new Response.ErrorListener() {
//            // logs error
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                NetworkResponse errorNetworkResponse = volleyError.networkResponse;
//                String errorData = "";
//                try {
//                    if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
//                        errorData = new String(errorNetworkResponse.data, "UTF-8");
//                    }
//                } catch(Exception e) {
//                    Log.e("Error", e.toString());
//                }
//                Log.e("Volley error", errorData);
//            }
//        };
//        return responseListener;
//    }

    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }

    public String toString() {
        return "Heading: " + heading + "\nSubheading: " + subheading;
    }




}

