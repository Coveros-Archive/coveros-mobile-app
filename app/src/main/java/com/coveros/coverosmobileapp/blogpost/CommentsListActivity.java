package com.coveros.coverosmobileapp.blogpost;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
 * @author Maria Kim
 */

public class CommentsListActivity extends ListActivity {

    private AlertDialog errorMessage;
    private RequestQueue rQueue;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);

        final String postId = getIntent().getExtras().getString("postId");

        commentListView = getListView();
        TextView textView = new TextView(CommentsListActivity.this);
        textView.setText("Comments");
        textView.setTextSize(20);
        textView.setPadding(0,0,0,30);

        commentListView.addHeaderView(textView);

//        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=" + postId;
        final String COMMENTS_URL = "http://www.dev.secureci.com/wp-json/wp/v2/comments?post=6600";  // hard coding for development

        // constructing errorMessage dialog for activity
        errorMessage = new AlertDialog.Builder(CommentsListActivity.this).create();
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

        Thread commentRequest = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(CommentsListActivity.this);
                retrieveComments(new PostReadCallback<Comment>() {
                    @Override
                    public void onSuccess(List<Comment> newComments) {
                        Log.d("COMMENTS", "COMMENTS ADAPTER ADDED");
                        Log.d("NEW COMMENTS", newComments.toString());
                        if (newComments.size() == 0) {
                            newComments.add(new Comment("", "", "No comments to display."));
                        }
                        newComments.add(new Comment("Maria", "2017-06-06T10:23:18", "<p>Awesome post! Way better than anything Ryan could write.</p>"));
                        newComments.add(new Comment("Ethan", "2017-06-06T10:23:18", "<p>Why aren't the lights on?</p>"));
                        newComments.add(new Comment("Sadie", "2017-06-06T10:23:18", "<p>I don't know, felt like it DRONEd on...</p>"));
                        CommentsListAdapter commentsAdapter = new CommentsListAdapter(CommentsListActivity.this, R.layout.comment_list_text, newComments);
                        commentListView.setAdapter(commentsAdapter);
                        Log.d("NOTICE", "PAST SETTING ADAPTER");
                        Log.d("COMMENTS", "" +  commentListView.isShown());
                        Log.d("NUM OF COMMENTS" , "" + commentListView.getAdapter().getCount());
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
        Log.d("NOTICE", "RETRIEVE COMMENTS BEING EXECUTED");
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
