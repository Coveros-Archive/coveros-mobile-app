package com.coveros.coverosmobileapp.blogpost;

import android.os.Bundle;

import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
 * @author Maria Kim
 */

public class CommentsListActivity extends BlogListActivity {

    private RequestQueue rQueue;
    private ListView commentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);

        commentListView = getListView();
        commentListView.addHeaderView(createTextViewLabel(CommentsListActivity.this, "Comments"));  // setting label above comments list

        errorListener = createErrorListener(CommentsListActivity.this);
//        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=" + getIntent().getExtras().getString("postId");
        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=6600";  // hard coding for development


        Thread commentRequest = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(CommentsListActivity.this);
                retrieveComments(new PostReadCallback<Comment>() {
                    @Override
                    public void onSuccess(List<Comment> newComments) {
                        if (newComments.size() == 0) {
                            newComments.add(new Comment("", "", "No comments to display."));
                        }
                        // adding example comments for development purposes
                        newComments.add(new Comment("Maria", "2017-06-06T10:23:18", "<p>Awesome post! Way better than anything Ryan could write.</p>"));
                        newComments.add(new Comment("Ethan", "2017-06-06T10:23:18", "<p>Why aren't the lights on?</p>"));
                        newComments.add(new Comment("Sadie", "2017-06-06T10:23:18", "<p>I don't know, felt like it DRONEd on...</p>"));
                        CommentsListAdapter commentsAdapter = new CommentsListAdapter(CommentsListActivity.this, R.layout.comment_list_text, newComments);
                        commentListView.setAdapter(commentsAdapter);
                    }
                }, COMMENTS_URL);
            }
        };
        commentRequest.start();
    }

    /**
     * Passes List of Comments for the BlogPost from Wordpress to callback.
     * @param postReadCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveComments(final PostReadCallback<Comment> postReadCallback, String COMMENTS_URL) {
        StringRequest commentsRequest = new StringRequest(Request.Method.GET, COMMENTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Comment> comments = new ArrayList<>();
                JsonArray commentsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement comment : commentsJson) {
                    comments.add(new Comment((JsonObject) comment));
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
