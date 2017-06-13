package com.coveros.coverosmobileapp;

import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress

/**
 * Created by Maria Kim on 6/9/2017
 * Creates ListView that displays list of titles of blog posts.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */
public class PostList extends AbstractPostActivity {

    final static String url = "http://www.coveros.com/wp-json/wp/v2/posts?fields=id,title";
    ArrayList responseList;
    Map<String, Object> mapPost;

    AlertDialog errorMessage;

    ListView postList;
    int postId;

    public PostList() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        postList = (ListView) findViewById(R.id.postList);

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Gson responseGson = new Gson();
            Log.d("Response string", response);
            responseList = (ArrayList) responseGson.fromJson(response, ArrayList.class);

            postList.setAdapter(new ArrayAdapter(PostList.this, android.R.layout.simple_list_item_1, getPostTitles(responseList)));

        }, getErrorListener(PostList.this));

        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(request);

        postList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            mapPost = (Map<String, Object>) responseList.get(position);
            postId = ((Double) mapPost.get("id")).intValue();

            Intent intent = new Intent(getApplicationContext(), Post.class);
            intent.putExtra("id", "" + postId);
            startActivity(intent);
        });

    }

    protected String[] getPostTitles(ArrayList responseList) {
        String [] postTitles = new String[responseList.size()];

        for (int i = 0; i < responseList.size(); i++) {
            mapPost = (Map<String, Object>) responseList.get(i);
            Log.d("Post Info", Arrays.toString(mapPost.entrySet().toArray()));
            Map<String, Object> mapTitle = (Map<String, Object>) mapPost.get("title");
            postTitles[i] = StringEscapeUtils.unescapeHtml4(mapTitle.get("rendered").toString());
        }

        return postTitles;
    }

    protected void setErrorMessage(AlertDialog errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected AlertDialog getErrorMessage() {
        return errorMessage;
    }
}


