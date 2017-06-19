package com.coveros.coverosmobileapp;

import android.content.Context;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by maria on 6/16/2017.
 */

public class Post {

    String title, date, heading, subheading;
    Author author;
    int id;


    public Post(String title, String date, Author author, int id) {
        this.title = title;
        try {
            this.date = formatDate(date);
        } catch (ParseException e) {
            Log.e("Parse exception", e.toString());
        }
        this.author = author;
        this.id = id;

        heading = StringEscapeUtils.unescapeHtml4(this.title);
        subheading = StringEscapeUtils.unescapeHtml4(this.author + "\t" + this.date);
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Date parsedDate = dateFormatter.parse(date);
        SimpleDateFormat datePrint = new SimpleDateFormat("MMM d, yyyy");
        return datePrint.format(parsedDate);
    }


    public int getId() { return id; }
    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }

    public String toString() {
        return "Heading: " + heading + "\nSubheading: " + subheading;
    }




}

