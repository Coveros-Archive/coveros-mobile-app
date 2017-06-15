package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Maria Kim on 6/9/2017
 * Creates ListView that displays list of titles of blog posts.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */
public class PostListView extends AbstractPostActivity {

    JsonArray responseList;
    ListView postsListView;
    Intent currentPostIntent;
    ArrayAdapter postsAdapter;
    List<String> postTitles = new ArrayList<>();
    int postsPerPage = 10;
    int offset = 0;

    String url = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&fields=id,title&offset=" + offset;

    public Posts() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        postsListView = (ListView) findViewById(R.id.postList);
        postsListView.setStackFromBottom(true);

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            responseList = new JsonParser().parse(response).getAsJsonArray();
                postTitles.addAll(getPostTitles(responseList));
            postsAdapter = new ArrayAdapter(Posts.this, android.R.layout.simple_list_item_1, getPostTitles(responseList));
            postsListView.setAdapter(postsAdapter);
                offset = offset + postsPerPage;
            }
        }, getErrorListener(Posts.this));

        RequestQueue rQueue = Volley.newRequestQueue(Posts.this);
        rQueue.add(request);

        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject post = (JsonObject) responseList.get(position);
                int postId = post.get("id").getAsInt();

                Intent intent = new Intent(getApplicationContext(), Post.class);
                intent.putExtra("id", "" + postId);
                currentPostIntent = intent;
                startActivity(intent);
            }
        });

        postsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    addOlderPosts();
                }
            }
        });
    }

    protected void addOlderPosts() {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                offset = offset + postsPerPage;
                responseList = new JsonParser().parse(response).getAsJsonArray();
                postsAdapter.add(getPostTitles(responseList));
                postsListView.setAdapter(postsAdapter);
            }
        }, getErrorListener(Posts.this));
    }

    protected List getPostTitles(JsonArray responseList) {
        List<String> postTitles = new ArrayList<>();
        JsonObject title;

        for (int i = 0; i < responseList.size(); i++) {
            title = (JsonObject) responseList.get(i).getAsJsonObject().get("title");
            postTitles.add(StringEscapeUtils.unescapeHtml4(title.get("rendered").getAsString()));
        }

        return postTitles;
    }

    protected Intent getCurrentPostIntent() {
        return currentPostIntent;
    }


    public ListView getPostsListView() { return postsListView; }

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
}


