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
import java.text.ParseException;
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

public class Post {

    String title, date, heading, subheading;
    Author author;


    public Post(String title, String date, Author author) throws Exception {
        this.title = title;
        try {
            this.date = formatDate(date);
        } catch (ParseException e) {
            Log.e("Parse exception", e.toString());
        }
        this.author = author;

        heading = StringEscapeUtils.unescapeHtml4(title);
        subheading = StringEscapeUtils.unescapeHtml4(author + "\t" + date);
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        return datePrint.format(parsedDate);
    }

    public static void retrieveAuthor(final PostList.VolleyCallback callback, int id, Context context) throws Exception {

        String url = "https://www.dev.secureci.com/wp-json/wp/v2/users/" + id;
        Log.d("AUTHOR ID", Integer.toString(id));

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject authorJson = new JsonParser().parse(response).getAsJsonObject();
                callback.onSuccess(authorJson);

//                Log.d("Author: ", authorName);
            }
        }, getErrorListener());
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }


    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }

    public String toString() {
        return "Heading: " + heading + "\nSubheading: " + subheading;
    }




}

