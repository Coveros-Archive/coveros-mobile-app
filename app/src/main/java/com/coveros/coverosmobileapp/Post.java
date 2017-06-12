package com.coveros.coverosmobileapp;

import android.os.Bundle;

import android.webkit.WebView;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Map;


/**
 * Created by Maria Kim on 6/9/2017.
 * Creates and displays a single blog post when it is selected from the list of blog posts.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */

public class Post extends AbstractPostActivity {
    TextView title;
    WebView content;

    /**
     * When post is created (through selection), GETs data for post via Wordpress' REST API and displays title and content.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final String id = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);

        String url = "http://www.coveros.com/wp-json/wp/v2/posts/" + id + "?fields=title,content";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Gson responseGson = new Gson();
            Map<String, Object> mapPost = (Map<String, Object>) responseGson.fromJson(response, Map.class);
            Map<String, Object> mapTitle = (Map<String, Object>) mapPost.get("title");
            Map<String, Object> mapContent = (Map<String, Object>) mapPost.get("content");

            title.setText(StringEscapeUtils.unescapeHtml4(mapTitle.get("rendered").toString()));
            content.loadData(StringEscapeUtils.unescapeHtml4(mapContent.get("rendered").toString()), "text/html; charset=utf-8", "UTF-8");
        }
        , getErrorListener(Post.this));

        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);
    }
}
