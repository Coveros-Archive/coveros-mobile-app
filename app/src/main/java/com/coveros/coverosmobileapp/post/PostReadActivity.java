package com.coveros.coverosmobileapp.post;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.AbsListView;
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

    private RequestQueue rQueue;
    private AlertDialog errorMessage;
    private ListView commentListView;

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            // displays errorMessage
            errorMessage.show();
            NetworkResponse errorNetworkResponse = volleyError.networkResponse;
            String errorData = "";
            if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
                errorData = new String(errorNetworkResponse.data);
            }
            Log.e("Volley error", errorData);
        }
    };

    /**
     * Grabs post data from Intent and displays it and its comments.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        commentListView = (ListView) findViewById(R.id.comments);

        // constructing errorMessage dialog for activity
        errorMessage = new AlertDialog.Builder(PostReadActivity.this).create();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("An error occurred.");
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        List<String> post = getIntent().getStringArrayListExtra("postData");

//        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=" + post.get(ID_KEY);
        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=6600";  // hard coding for development

        TextView heading = (TextView) findViewById(R.id.heading);
        TextView subheading = (TextView) findViewById(R.id.subheading);
//        WebView content = (WebView) findViewById(R.id.content);

        heading.setText(post.get(HEADING_KEY));
        subheading.setText(post.get(SUBHEADING_KEY));
//        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8");

        Thread commentRequest = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(PostReadActivity.this);
                retrieveComments(new PostReadCallback<Comment>() {
                    @Override
                    public void onSuccess(List<Comment> newComments) {
                        Log.d("COMMENTS", "COMMENTS ADAPTER ADDED");
                        Log.d("NEW COMMENTS", newComments.toString());
                        if (newComments.size() == 0) {
                            newComments.add(new Comment("", "", "No comments to display."));
                        }
                        CommentAdapter commentsAdapter = new CommentAdapter(PostReadActivity.this, R.layout.comment_text, newComments);
                        commentListView.setAdapter(commentsAdapter);
                        Log.d("COMMENTS", "" +  commentListView.isShown());
                    }
                }, COMMENTS_URL);
            }
        };
        commentRequest.start();

    }

    /**
     * Passes List of Comments for the Post from Wordpress to callback.
     * @param postReadCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveComments(final PostReadCallback<Comment> postReadCallback, String COMMENTS_URL) {
        StringRequest commentsRequest = new StringRequest(Request.Method.GET, COMMENTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Comment> comments = new ArrayList<>();
                JsonArray commentsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement comment : commentsJson) {
                    JsonObject commentJson = (JsonObject) comment;
                    String name = commentJson.get("author_name").getAsString();
                    String date = commentJson.get("date").getAsString();
                    String content = commentJson.get("content").getAsJsonObject().get("rendered").getAsString();

                    comments.add(new Comment(name, date, content));
                }
                postReadCallback.onSuccess(comments);
            }
        }, errorListener);
        rQueue.add(commentsRequest);
    }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    interface PostReadCallback<T> {
        void onSuccess(List<T> newItems);
    }
}
