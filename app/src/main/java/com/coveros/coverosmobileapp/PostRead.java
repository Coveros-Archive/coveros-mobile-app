package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maria Kim on 6/9/2017.
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */

public class PostRead extends AppCompatActivity {

    AlertDialog errorMessage;
    TextView heading, subheading;
    WebView content;
    final int HEADING = 0;
    final int SUBHEADING = 1;
    final int CONTENT = 2;

    /**
     * When post is created (through selection), GETs data for post via Wordpress' REST API and displays title and content.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        ArrayList<String> post = getIntent().getStringArrayListExtra("postData");

        // constructing errorMessage dialog for activity
        errorMessage = new AlertDialog.Builder(PostRead.this).create();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("An error occurred.");
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        heading = (TextView) findViewById(R.id.heading);
        subheading = (TextView) findViewById(R.id.subheading);
        content = (WebView) findViewById(R.id.content);

        heading.setText(post.get(HEADING));
        subheading.setText(post.get(SUBHEADING));
        content.loadData(post.get(CONTENT), "text/html; charset=utf-8", "UTF-8");


    }

}
