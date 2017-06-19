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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.coveros.coverosmobileapp.PostList.getActivity;

/**
 * Created by maria on 6/16/2017.
 */

public class PostMetaData {
    AlertDialog errorMessage;

    String heading, subheading;
    String author;


    public PostMetaData(JsonObject postJson, Context context) throws Exception {
        String title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
        retrieveAuthor(postJson.get("author").getAsInt(), context); // I don't like this as a void method, but leaving for now
//        String author = postJson.get("author").getAsString();
        String date = postJson.get("date").getAsString();
        heading = StringEscapeUtils.unescapeHtml4(title);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        subheading = StringEscapeUtils.unescapeHtml4(author + "\t \t \t" + datePrint.format(parsedDate));

    }

    public void retrieveAuthor(int id, Context context) throws Exception {

        String url = "https://www.dev.secureci.com/wp-json/wp/v2/users/" + id;
        Log.d("AUTHOR ID", Integer.toString(id));


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject authorJson = new JsonParser().parse(response).getAsJsonObject();
                author = authorJson.get("name").getAsString();
                Log.d("Author: ", author);
            }
        }, getErrorListener());
        RequestQueue rQueue = Volley.newRequestQueue(context);
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

    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }

    public String toString() {
        return "Heading: " + heading + "\nSubheading: " + subheading;
    }




}

