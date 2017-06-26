package com.coveros.coverosmobileapp.post;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.coveros.coverosmobileapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
     * Grabs post data from Intent and displays it.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        List<String> post = getIntent().getStringArrayListExtra("postData");

        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=" + post.get(ID_KEY);

        TextView heading = (TextView) findViewById(R.id.heading);
        TextView subheading = (TextView) findViewById(R.id.subheading);
        WebView content = (WebView) findViewById(R.id.content);

        heading.setText(post.get(HEADING_KEY));
        subheading.setText(post.get(SUBHEADING_KEY));
        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8");


    }

    /**
     * Populates List of Authors.
     * @param postListCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveAuthors(final PostListActivity.PostListCallback<String> postListCallback, String commentsUrl) {
        StringRequest authorsRequest = new StringRequest(Request.Method.GET, AUTHORS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray authorsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement author : authorsJson) {
                    JsonObject authorJson = (JsonObject) author;
                    Integer id = authorJson.get("id").getAsInt();
                    authors.put(id, authorJson.get("name").getAsString());
                }
                postListCallback.onSuccess(null);
            }
        }, getErrorListener());
        rQueue.add(authorsRequest);
    }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    interface PostReadCallback<T> {
        void onSuccess(List<T> newItem);
    }
}
