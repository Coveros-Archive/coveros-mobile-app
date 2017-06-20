package com.coveros.coverosmobileapp;

import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import android.widget.TextView;

import java.util.ArrayList;


/**
 * @author Maria Kim
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 */
public class PostReadActivity extends AppCompatActivity {

    TextView heading, subheading;
    WebView content;
    final int HEADING = 0;
    final int SUBHEADING = 1;
    final int CONTENT = 2;
    final int POSITION = 3;

    String position; // for instrumented test
    /**
     * Grabs post data from Intent and displays it.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        ArrayList<String> post = getIntent().getStringArrayListExtra("postData");

        String position = post.get(POSITION);

        heading = (TextView) findViewById(R.id.heading);
        subheading = (TextView) findViewById(R.id.subheading);
        content = (WebView) findViewById(R.id.content);

        heading.setText(post.get(HEADING));
        subheading.setText(post.get(SUBHEADING));
        content.loadData(post.get(CONTENT), "text/html; charset=utf-8", "UTF-8");


    }

    /**
     * For instrumented test to check that correct post is being displayed.
     */
    public String getPosition() { return position; }

}
