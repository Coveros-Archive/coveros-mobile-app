package com.coveros.coverosmobileapp;

import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
    List responseList;
    Map<String, Object> mapPost;

    ListView postList;
    int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        postList = (ListView) findViewById(R.id.postList);

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Gson responseGson = new Gson();
            responseList = (List) responseGson.fromJson(response, List.class);

            String[] postTitles = new String[responseList.size()];

            for (int i = 0; i < responseList.size(); ++i) {
                mapPost = (Map<String, Object>) responseList.get(i);
                Map<String, Object> mapTitle = (Map<String, Object>) mapPost.get("title");
                postTitles[i] = StringEscapeUtils.unescapeHtml4(mapTitle.get("rendered").toString());
            }

            postList.setAdapter(new ArrayAdapter(PostList.this, android.R.layout.simple_list_item_1, postTitles));

        }, getErrorListener(PostList.this));

        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(request);

        postList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    mapPost = (Map<String, Object>) responseList.get(position);
                    postId = ((Double) mapPost.get("id")).intValue();

                    Intent intent = new Intent(getApplicationContext(), Post.class);
                    intent.putExtra("id", "" + postId);
                    startActivity(intent);
                }
        );

    }
}


