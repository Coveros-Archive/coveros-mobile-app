package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 *
 * @author Maria Kim
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogPostReadActivity extends AppCompatActivity {

    /**
     * Grabs post data from Intent and displays it and its comments.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        final int blogId = getIntent().getIntExtra("blogId", 0);
        final String blogPost = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + blogId;
        RequestQueue rQueue = Volley.newRequestQueue(BlogPostReadActivity.this);
        StringRequest blogPostsRequest = new StringRequest(Request.Method.GET, blogPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject blogPostsJson = new JsonParser().parse(response).getAsJsonObject();
                WebView content = (WebView) findViewById(R.id.content);
                content.loadData(blogPostsJson.get("content").getAsJsonObject().get("rendered").getAsString(), "text/html, charset=utf-8", "UTF-8");
                setTitle(blogPostsJson.get("title").getAsJsonObject().get("rendered").getAsString());
            }
        }, new BlogPostErrorListener(BlogPostReadActivity.this));
        rQueue.add(blogPostsRequest);

        Button viewComments = (Button) findViewById(R.id.view_comments);

        // when user clicks on "View comments" button, open up CommentsListActivity to display comments for this post
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsListActivity.class);
                intent.putExtra("postId", Integer.toString(blogId));
                startActivity(intent);
            }
        });
    }
}
