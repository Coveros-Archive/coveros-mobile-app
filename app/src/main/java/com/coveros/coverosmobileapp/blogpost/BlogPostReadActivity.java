package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.errorlistener.ErrorListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 *
 * @author Maria Kim
 */
public class BlogPostReadActivity extends AppCompatActivity {
    private static final int NUM_OF_AUTHORS = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    private static final String AUTHORS_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + NUM_OF_AUTHORS;
    private SparseArray<String> authors = new SparseArray<>();  // to aggregate the ids and names of the authors of displayed blog posts
    private RequestQueue rQueue = Volley.newRequestQueue(BlogPostReadActivity.this);

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

        retrieveAuthors(new PostListCallback<String>() {
            @Override
            public void onSuccess(List<String> newAuthors) {
                StringRequest blogPostsRequest = new StringRequest(Request.Method.GET, blogPost, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject blogPostsJson = new JsonParser().parse(response).getAsJsonObject();
                        BlogPost post = new BlogPost(blogPostsJson, authors);
                        WebView content = (WebView) findViewById(R.id.content);
                        String textContent = StringEscapeUtils.unescapeHtml4(blogPostsJson.get("content").getAsJsonObject().get("rendered").getAsString());
                        content.loadData(textContent, "text/html; charset=utf-8", "UTF-8");
                        setTitle(blogPostsJson.get("title").getAsJsonObject().get("rendered").getAsString());
                        post.getContent();
                        post.getTitle();
                    }
                }, new ErrorListener(BlogPostReadActivity.this));
                rQueue.add(blogPostsRequest);
            }
        });

        Button viewComments = (Button) findViewById(R.id.view_comments);

        // when user clicks on "View comments" button, open up CommentsListActivity to display comments for this post
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsListActivity.class);
                intent.putExtra("postId", "" + blogId);
                startActivity(intent);
            }
        });
    }

    /**
     * Populates List of Authors.
     *
     * @param postListCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveAuthors(final PostListCallback<String> postListCallback) {
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
        }, new ErrorListener(BlogPostReadActivity.this));
        rQueue.add(authorsRequest);
    }

    interface PostListCallback<T> {
        void onSuccess(List<T> newItems);
    }

}
