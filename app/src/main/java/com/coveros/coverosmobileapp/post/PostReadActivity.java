package com.coveros.coverosmobileapp.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;


/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 * @author Maria Kim
 */
public class PostReadActivity extends AppCompatActivity {

    static final int HEADING_KEY = 0;
    static final int SUBHEADING_KEY = 1;
    static final int CONTENT_KEY = 2;
    static final int ID_KEY = 4;


    /**
     * Grabs post data from Intent and displays it and its comments.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);


        final List<String> post = getIntent().getStringArrayListExtra("postData");


        WebView content = (WebView) findViewById(R.id.content);

        setTitle(post.get(HEADING_KEY));
        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8");

        Button viewComments = (Button) findViewById(R.id.view_comments);
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentListActivity.class);
                intent.putExtra("postId", "" + post.get(ID_KEY));
                startActivity(intent);
            }
        });

    }


}
