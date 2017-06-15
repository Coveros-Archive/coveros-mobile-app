package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Maria Kim on 6/9/2017
 * Creates ListView that displays list of titles of blog posts.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */
public class Posts extends AbstractPostActivity {

    final static String url = "https://www.dev.secureci.com/wp-jsonwp/v2/posts?fields=id,title";

    JsonArray responseList;

    ListView postsListView;

    Intent currentPostIntent;

    private boolean isInFront;

    public Posts() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        postsListView = (ListView) findViewById(R.id.postList);

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            responseList = new JsonParser().parse(response).getAsJsonArray();
            postsListView.setAdapter(new ArrayAdapter(Posts.this, android.R.layout.simple_list_item_1, getPostTitles(responseList)));

        }, getErrorListener(Posts.this));

        RequestQueue rQueue = Volley.newRequestQueue(Posts.this);
        rQueue.add(request);

        postsListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            JsonObject post = (JsonObject) responseList.get(position);
            int postId =  post.get("id").getAsInt();

            Intent intent = new Intent(getApplicationContext(), Post.class);
            intent.putExtra("id", "" + postId);
            currentPostIntent = intent;
            startActivity(intent);
        });

    }


    protected String[] getPostTitles(JsonArray responseList) {
        String [] postTitles = new String[responseList.size()];
        JsonObject title;

        for (int i = 0; i < responseList.size(); i++) {
            title = (JsonObject) responseList.get(i).getAsJsonObject().get("title");
            postTitles[i] = StringEscapeUtils.unescapeHtml4(title.get("rendered").getAsString());
        }

        return postTitles;
    }

    protected Intent getCurrentPostIntent() {
        return currentPostIntent;
    }

    public boolean getIsInFront() {
        return isInFront;
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


