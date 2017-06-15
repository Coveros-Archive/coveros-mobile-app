package com.coveros.coverosmobileapp;

import android.os.Bundle;

import android.webkit.WebView;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    private boolean isActive;

    private String id;

    /**
     * When post is created (through selection), GETs data for post via Wordpress' REST API and displays title and content.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        id = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);

        String url = "https://www.dev.secureci.com/wp-json/wp/v2/posts/" + id + "fields=title,content";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            JsonObject post = new JsonParser().parse(response).getAsJsonObject();
            JsonObject titleText = post.get("title").getAsJsonObject();
            JsonObject contentText = post.get("content").getAsJsonObject();

            title.setText(StringEscapeUtils.unescapeHtml3(titleText.get("rendered").getAsString()));
            content.loadData(StringEscapeUtils.unescapeHtml3(contentText.get("rendered").getAsString()), "text/html; charset=utf-8", "UTF-8");
        }
        , getErrorListener(Post.this));

        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);
    }

    public String getId() {
        return id;
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
