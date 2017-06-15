package com.coveros.coverosmobileapp;

import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress

/**
 * Created by Maria Kim on 6/9/2017
 * Creates ListView that displays list of titles of blog posts.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */
public class PostList extends AbstractPostActivity {

    final static String url = "https://www.dev.secureci.com/wp-jsonwp/v2/posts?fields=id,title";

    JsonArray responseList;

    AlertDialog errorMessage;
    ListView postList;

    private boolean isInFront;

    public PostList() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        postList = (ListView) findViewById(R.id.postList);

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            responseList = new JsonParser().parse(response).getAsJsonArray();
            postList.setAdapter(new ArrayAdapter(PostList.this, android.R.layout.simple_list_item_1, getPostTitles(responseList)));

        }, getErrorListener(PostList.this));

        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(request);

        postList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            JsonObject post = (JsonObject) responseList.get(position);
            int postId =  post.get("id").getAsInt();

            Intent intent = new Intent(getApplicationContext(), Post.class);
            intent.putExtra("id", "" + postId);
            startActivity(intent);
        });

    }

    protected String[] getPostTitles(JsonArray responseList) {
        String [] postTitles = new String[responseList.size()];
        JsonObject title;

        for (int i = 0; i < responseList.size(); i++) {
            title = (JsonObject) responseList.get(i).getAsJsonObject().get("title");
            postTitles[i] = StringEscapeUtils.unescapeHtml4(title.get("rendered").getAsString());
        }

        return postTitles;
    }

    protected AlertDialog getErrorMessage() {
        return errorMessage;
    }


    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = true;
    }


    public boolean getIsInFront() {
        return isInFront;
    }
}


