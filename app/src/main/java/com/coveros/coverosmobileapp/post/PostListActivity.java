package com.coveros.coverosmobileapp.post;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
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

import com.coveros.coverosmobileapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Maria Kim
 * Creates ListView that displays list of titles of blog post_list.
 */
public class PostListActivity extends ListActivity {

    private List<Post> posts = new ArrayList<>();
    private SparseArray<String> authors = new SparseArray<>();
    private RequestQueue rQueue;

    private ListView postListView;
    private PostListAdapter postsAdapter;

    private int currentListSize;

    private AlertDialog errorMessage;

    private static final int POSTS_PER_PAGE = 10;
    private int postsOffset = 0;

    private static final int NUM_OF_AUTHORS = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    private static final String AUTHORS_URL = "https://www.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + NUM_OF_AUTHORS;

    private static final String POSTS_URL = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + POSTS_PER_PAGE + "&order=desc&orderby=date&fields=id,title,date,author&offset=%d";

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
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.post_list);

        postListView = getListView();

        // constructing errorMessage dialog for activity
        errorMessage = new AlertDialog.Builder(PostListActivity.this).create();
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

        // running these requests on a separate thread for performance
        Thread requests = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(PostListActivity.this);
                retrieveAuthors(new PostListCallback<String>() {
                    @Override
                    public void onSuccess(List<String> newAuthors) {
                        retrievePosts(new PostListCallback<Post>() {
                            @Override
                            public void onSuccess(List<Post> newPosts) {
                                posts.addAll(newPosts);
                                postsAdapter = new PostListAdapter(PostListActivity.this, R.layout.post_list_text, posts);
                                postListView.setAdapter(postsAdapter);
                                currentListSize = postListView.getAdapter().getCount();
                                setScrollListener();
                            }

                        });
                    }
                });
            }
        };
        requests.start();

        // when a post is selected, feeds its associated data into a PostReadActivity activity
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = posts.get(position);
                ArrayList<String> postData = new ArrayList<>();
                postData.add(post.getHeading());
                postData.add(post.getSubheading());
                postData.add(post.getContent());
                postData.add(String.valueOf(position));
                postData.add(String.valueOf(post.getId()));
                Intent intent = new Intent(getApplicationContext(), PostReadActivity.class);
                intent.putStringArrayListExtra("postData", postData);
                startActivity(intent);
            }
        });
    }

    /**
     * Populates List of Authors.
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
        }, errorListener);
        rQueue.add(authorsRequest);
    }

    /**
     * Populates List of Posts.
     * @param postListCallback A callback function to be executed after the list of posts has been retrieved
     */
    protected void retrievePosts(final PostListCallback<Post> postListCallback) {
        StringRequest postsRequest = new StringRequest(Request.Method.GET, String.format(Locale.US, POSTS_URL, postsOffset), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray postsJson = new JsonParser().parse(response).getAsJsonArray();
                List<Post> newPosts = new ArrayList<>();
                for (JsonElement post : postsJson) {
                    JsonObject postJson = (JsonObject) post;
                    String title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
                    String date = postJson.get("date").getAsString();
                    int authorId = postJson.get("author").getAsInt();
                    int id = postJson.get("id").getAsInt();
                    String content = postJson.get("content").getAsJsonObject().get("rendered").getAsString();
                    newPosts.add(new Post(title, date, authors.get(authorId), id, content));
                }
                postListCallback.onSuccess(newPosts);
                postsOffset = postsOffset + POSTS_PER_PAGE;
            }
        }, errorListener);

        rQueue.add(postsRequest);
    }

    /**
     * Sets the scroll listener for the list view. When the user scrolls to the bottom, calls method to load more posts by the specified increment (POSTS_PER_PAGE)
     */
    private void setScrollListener() {
        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean firstScroll = true;  // first time scrolling to bottom

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (postListView.getAdapter() != null) {
                    if (firstScroll) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                            addPosts();
                            firstScroll = false;
                        }
                    } else {
                        // ensures new posts are loaded only once per time the bottom is reached (i.e. if the user continuously scrolls to the bottom, more than "POSTS_PER_PAGE" posts will not be loaded
                        if (postListView.getAdapter().getCount() == currentListSize + POSTS_PER_PAGE) {
                            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                                addPosts();
                                currentListSize = postListView.getAdapter().getCount();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds posts to the list view. Called when user scrolls to the bottom of the listview.
     */
    protected void addPosts() {
        Thread addPostRequest = new Thread() {
            @Override
            public void run() {
                retrievePosts(new PostListCallback<Post>() {
                    @Override
                    public void onSuccess (List <Post> newPosts) {
                        postsAdapter.addAll(newPosts);
                        postsAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        addPostRequest.start();
    }

    public AlertDialog getErrorMessage() {
        return errorMessage;
    }

    public ListView getPostListView() {
        return postListView;
    }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    interface PostListCallback<T> {
        void onSuccess(List<T> newItems);
    }
}


