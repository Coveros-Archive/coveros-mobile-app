package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Maria Kim on 6/9/2017
 * Creates ListView that displays list of titles of blog post_list.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */
public class PostList extends ListActivity {

    JsonArray responseList;
    List<PostMetaData> posts = new ArrayList<>();

    ListView postListView;
    PostListAdapter postsAdapter;

    int postsPerPage = 10;
    int offset = 0;

    int previousPostListViewSize;
    boolean first = true;

    AlertDialog errorMessage;

    String url = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&fields=id,title,date,author&offset=" + offset;

    public PostList() {
    }

    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.post_list);

        postListView = getListView();

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, getListener(), getErrorListener(PostList.this));

        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(request);

//        Log.d("PostMetaData: ", posts.get(0).toString());

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject post = (JsonObject) responseList.get(position);
                int postId = post.get("id").getAsInt();

                Intent intent = new Intent(getApplicationContext(), Post.class);
                intent.putExtra("id", "" + postId);
                startActivity(intent);
            }
        });

        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (postListView.getAdapter() != null) {
                    if (first) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                            addPosts();
                        }
                    }
                    else {
                        // ensures new posts are loaded only once per time the bottom is reached (i.e. if the user continuously scrolls to the bottom, more than "postsPerPage" posts will not be loaded
                        if (postListView.getAdapter().getCount() == previousPostListViewSize + postsPerPage) {
                            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                                addPosts();
                                previousPostListViewSize = postListView.getAdapter().getCount();
                            }
                        }
                    }
                }
            }
        });
    }

    protected void addPosts() {

        url = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&fields=id,title,date,author&offset=" + offset;
        first = false;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                offset = offset + postsPerPage;
                JsonArray newResponseList = new JsonParser().parse(response).getAsJsonArray();
                responseList.addAll(newResponseList);
                postsAdapter.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }
        }, getErrorListener(PostList.this));
        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(request);
    }

    @VisibleForTesting
    public static Activity getActivity() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        HashMap activities = (HashMap) activitiesField.get(activityThread);
        for(Object activityRecord:activities.values()){
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if(!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return new Activity();
    }

    private Response.ErrorListener getErrorListener(final Context context) {
        Response.ErrorListener responseListener = new Response.ErrorListener() {
            // logs error
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // creates error message to be displayed to user
                errorMessage = new AlertDialog.Builder(context).create();
                errorMessage.setTitle("Error");
                errorMessage.setMessage("An error occurred.");
                errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                errorMessage.show();
                NetworkResponse errorNetworkResponse = volleyError.networkResponse;
                String errorData = "";
                try {
                    if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
                        errorData = new String(errorNetworkResponse.data, "UTF-8");
                    }
                } catch(Exception e) {
                    Log.e("Error", e.toString());
                }
                Log.e("Volley error", errorData);
            }
        };
        return responseListener;
    }

    private Response.Listener<String> getListener() {
        Response.Listener<String> listener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseList = new JsonParser().parse(response).getAsJsonArray();
                try {
                    for (JsonElement responseJson : responseList) {
                        posts.add(new PostMetaData((JsonObject) responseJson, PostList.this));
                    }
                }
                catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                postsAdapter = new PostListAdapter(PostList.this, R.layout.post_list_text, posts);
                postListView.setAdapter(postsAdapter);
                offset = offset + postsPerPage;
                previousPostListViewSize = postListView.getAdapter().getCount();
            }
        };
        return listener;
    }

}


