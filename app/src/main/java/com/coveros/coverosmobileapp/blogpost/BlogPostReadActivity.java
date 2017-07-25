package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;
import com.coveros.coverosmobileapp.errorlistener.NetworkErrorListener;
import com.coveros.coverosmobileapp.oauth.RestRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 *
 * @author Maria Kim
 * @author Sadie Rynestad
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogPostReadActivity extends AppCompatActivity {

    private static final int NUM_OF_AUTHORS = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    private static final String AUTHORS_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + NUM_OF_AUTHORS;

    private SparseArray<String> authors = new SparseArray<>();  // to aggregate the ids and names of the authors of displayed blog posts
    private AlertDialog networkErrorAlertDialog;
    private NetworkErrorListener networkErrorListener;

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
        final String blogPostUrl = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + blogId;
        final RequestQueue requestQueue = Volley.newRequestQueue(BlogPostReadActivity.this);

        final String errorAlertDialogMessage = getString(R.string.blogpost_network_error_message);
        networkErrorAlertDialog = AlertDialogFactory.createNetworkErrorAlertDialogFinishButton(BlogPostReadActivity.this, errorAlertDialogMessage);
        networkErrorListener = new NetworkErrorListener(BlogPostReadActivity.this, networkErrorAlertDialog);

        RestRequest authorsRequest = new RestRequest(AUTHORS_URL, null, null, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                for (JsonElement author : response.get("response").getAsJsonArray()) {
                    JsonObject authorJson = (JsonObject) author;
                    Integer id = authorJson.get("id").getAsInt();
                    authors.put(id, authorJson.get("name").getAsString());
                }
                RestRequest blogPostRequest = new RestRequest(blogPostUrl, null, null, new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        BlogPost post = new BlogPost(response, authors);
                        WebView content = (WebView) findViewById(R.id.content);
                        content.loadData(post.getContent(), "text/html; charset=utf-8", "UTF-8");
                        setTitle(post.getTitle());
                    }
                }, networkErrorListener);
                requestQueue.add(blogPostRequest);
            }
        }, networkErrorListener);
        requestQueue.add(authorsRequest);

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
