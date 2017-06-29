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
 * Displays comments for a blog post.
 *
 * @author Maria Kim
 */

public class CommentsListActivity extends BlogListActivity {

    private RequestQueue rQueue;
    private ListView commentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);

        commentsListView = getListView();
        commentsListView.addHeaderView(createTextViewLabel(CommentsListActivity.this, "Comments"));  // setting label above comments list

        errorListener = createErrorListener(CommentsListActivity.this);
        final String commentsUrl = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=" + getIntent().getExtras().getString("postId");

        Thread commentRequest = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(CommentsListActivity.this);
                retrieveComments(new PostReadCallback<Comment>() {
                    @Override
                    public void onSuccess(List<Comment> newComments) {
                        if (newComments.isEmpty()) {
                            newComments.add(new Comment("", "", "<p>No comments to display.</p>"));
                        }
                        CommentsListAdapter commentsAdapter = new CommentsListAdapter(CommentsListActivity.this, R.layout.comment_list_text, newComments);
                        commentsListView.setAdapter(commentsAdapter);
                    }
                }, commentsUrl);
            }
        };
        commentRequest.start();
    }

    /**
     * Passes List of Comments for the BlogPost from Wordpress to callback.
     *
     * @param postReadCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveComments(final PostReadCallback<Comment> postReadCallback, String commentsUrl) {
        StringRequest commentsRequest = new StringRequest(Request.Method.GET, commentsUrl, new Response.Listener<String>() {
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

    ListView getCommentsListView() {
        return commentsListView;
    }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    interface PostReadCallback<T> {
        void onSuccess(List<T> newItems);
    }
}
