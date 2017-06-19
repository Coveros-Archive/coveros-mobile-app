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

    List<Post> posts = new ArrayList<>();
    HashMap<Integer, Author> authors = new HashMap<>();

    ListView postListView;
    PostListAdapter postsAdapter;

    int currentListSize;
    boolean first = true; // do I need this?

    final int postsPerPage = 10;
    final int postsOffset = 0;
    final String postsUrl = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&order=desc&orderby=date&fields=id,title,date,author&offset=" + postsOffset;
    final String authorsUrl = "https://www.dev.secureci.com/wp-json/wp/v2/users?orderby=id";

    public PostList() {
    }

    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.post_list);

        postListView = getListView();

        // creates List of Authors
        StringRequest usersRequest = new StringRequest(Request.Method.GET, authorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray authorsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement author : authorsJson) {
                    JsonObject authorJson = (JsonObject) author;
                    Integer id = new Integer(authorJson.get("id").getAsInt());
                    String name = authorJson.get("name").getAsString();
                    authors.put(id, new Author(authorJson.get("name").getAsString()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // creates error message to be displayed to user
                AlertDialog errorMessage = new AlertDialog.Builder(PostList.this).create();
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
                    Log.e("Volley error", e.toString());
                }
                Log.e("Volley error", errorData);
            }
        });

        // creates List of Posts
        StringRequest postsRequest = new StringRequest(Request.Method.GET, postsUrl, new Response.Listener<String>() {
            @Override
            public void OnResponse(String response) {
                JsonArray postsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement post : postsJson) {
                    JsonObject postJson = (JsonObject) post;

                    Post post = new Post()
                }
            }

        }, getErrorListener(PostList.this));





        title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
        String author = postJson.get("author").getAsString();
        String author = authorName;
        String date = postJson.get("date").getAsString();

        RequestQueue rQueue = Volley.newRequestQueue(PostList.this);
        rQueue.add(postsRequest);

//        Log.d("PostMetaData: ", posts.get(0).toString());

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject post = (JsonObject) responseList.get(position);
                int postId = post.get("id").getAsInt();

                Intent intent = new Intent(getApplicationContext(), PostRead.class);
                intent.putExtra("id", "" + postId);
                startActivity(intent);
            }
        });

    }

    private void setListViewScrollListener() {
        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (postListView.getAdapter() != null) {
                    if (first) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                            Log.d("First visible item", "" + firstVisibleItem);
                            Log.d("Visible item count", "" + visibleItemCount);
                            Log.d("Total item count", "" + totalItemCount);
//                            addPosts();
                            Log.d("DEBUGGING", "CALLING ADDPOSTS() AT FIRST");
                        }
                    } else {
                        // ensures new posts are loaded only once per time the bottom is reached (i.e. if the user continuously scrolls to the bottom, more than "postsPerPage" posts will not be loaded
                        if (postListView.getAdapter().getCount() == currentListSize + postsPerPage) {
                            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                                addPosts();
                                Log.d("DEBUGGING", "CALLING ADDPOSTS() NOT AT FIRST");
                                Log.d("First visible item", "" + firstVisibleItem);
                                Log.d("Visible item count", "" + visibleItemCount);
                                Log.d("Total item count", "" + totalItemCount);
                                currentListSize = postListView.getAdapter().getCount();
                            }
                        }
                    }
                }
            }
            });
    }


    protected void addPosts() {

        url = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&order=desc&orderby=date&fields=id,title,date,author&offset=" + offset;
        first = false;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                postsOffset = postsOffset + postsPerPage;
                JsonArray newResponseList = new JsonParser().parse(response).getAsJsonArray();
//                responseList.addAll(newResponseList);
                try {
                    for (JsonElement responseJson : newResponseList) {
                        final JsonObject responseJsonObject = (JsonObject) responseJson;
                        Post.retrieveAuthor(new VolleyCallback() {
                            String author;
                            @Override
                            public void onSuccess(JsonObject result) {
                                try {
                                    author = result.get("name").getAsString();
                                    Log.d("NAME: ", author);
                                    PostMetaData pmd = new PostMetaData((JsonObject) responseJsonObject, PostList.this, author);
                                    Log.d("POST META DATA ", pmd.toString());
                                    posts.add(pmd);
                                    postsAdapter = new PostListAdapter(PostList.this, R.layout.post_list_text, posts);
                                    postListView.setAdapter(postsAdapter);
                                    postsOffset = postsOffset + postsPerPage;
                                    currentListSize = postListView.getAdapter().getCount();

                                } catch (Exception e) {
                                    Log.e("ERROR", e.getMessage(), e);
                                }
                            }
                        }, responseJsonObject.get("author").getAsInt(), PostList.this);
                    }
                }
                catch (Exception e) {
                    Log.e("Error", e.toString());
                }

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
                        final JsonObject responseJsonObject = (JsonObject) responseJson;
                        Log.d("DATE", responseJsonObject.get("date").getAsString());
                        PostMetaData.retrieveAuthor(new VolleyCallback() {
                            String author;
                            @Override
                            public void onSuccess(JsonObject result) {
                                try {
                                    Log.d("ID", result.get("id").getAsString());
                                    author = result.get("name").getAsString();
                                    Log.d("NAME: ", author);
                                    PostMetaData pmd = new PostMetaData((JsonObject) responseJsonObject, PostList.this, author);
                                    Log.d("POST META DATA ", pmd.toString());
                                    posts.add(pmd);
                                    postsAdapter = new PostListAdapter(PostList.this, R.layout.post_list_text, posts);
                                    postListView.setAdapter(postsAdapter);
                                    postsOffset = postsOffset + postsPerPage;
                                    currentListSize = postListView.getAdapter().getCount();
                                    Log.d("Current list view count", "" + currentListSize);
                                    setListViewScrollListener();

                                } catch (Exception e) {
                                    Log.e("ERROR", e.getMessage(), e);
                                }
                            }
                        }, responseJsonObject.get("author").getAsInt(), PostList.this);
                    }
                }
                catch (Exception e) {
                    Log.e("Error", e.toString());
                }

            }
        };
        return listener;
    }

    public interface VolleyCallback {
        void onSuccess(JsonObject result);
    }

    public List<Post> getPosts() { return posts; }

}


