package com.coveros.coverosmobileapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;

// importing tools for WordPress integration

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Maria Kim
 * Creates ListView that displays list of titles of blog post_list.
 */
public class PostList extends ListActivity {

    List<Post> posts = new ArrayList<>();
    HashMap<Integer, Author> authors = new HashMap<>();
    RequestQueue rQueue;

    ListView postListView;
    PostListAdapter postsAdapter;

    int currentListSize;
    boolean firstScroll = true;  // first time scrolling to bottom

    AlertDialog errorMessage;

    final int postsPerPage = 10;
    int postsOffset = 0;

    int numOfAuthors = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    final String authorsUrl = "https://www.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + numOfAuthors;

    public PostList() {
    }

    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.post_list);

        postListView = getListView();

        // constructing errorMessage dialog for activity
        errorMessage = new AlertDialog.Builder(PostList.this).create();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("An error occurred.");
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        // running these requests on a separate thread for performance
        Thread requests = new Thread() {
            public void run() {
                rQueue = Volley.newRequestQueue(PostList.this);
                retrieveAuthors(new PostListCallback<Author>() {
                    @Override
                    public void onSuccess(List<Author> newAuthors) {
                        retrievePosts(new PostListCallback<Post>() {
                            @Override
                            public void onSuccess(List<Post> newPosts) {
                                posts.addAll(newPosts);
                                postsAdapter = new PostListAdapter(PostList.this, R.layout.post_list_text, posts);
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


        // when a post is selected, feeds its associated data into a PostRead activity
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = posts.get(position);

                ArrayList<String> postData = new ArrayList<String>();
                postData.add(post.getHeading());
                postData.add(post.getSubheading());
                postData.add(post.getContent());
                postData.add("" + position);
                Intent intent = new Intent(getApplicationContext(), PostRead.class);
                intent.putStringArrayListExtra("postData", postData);
                startActivity(intent);
            }
        });

    }

    /**
     * Populates List of Authors.
     * @param postListCallback
     */
    protected void retrieveAuthors(final PostListCallback postListCallback) {
        StringRequest authorsRequest = new StringRequest(Request.Method.GET, authorsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray authorsJson = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement author : authorsJson) {
                    JsonObject authorJson = (JsonObject) author;
                    Integer id = authorJson.get("id").getAsInt();
                    String name = authorJson.get("name").getAsString();
                    authors.put(id, new Author(authorJson.get("name").getAsString(), id.intValue()));
                }
                postListCallback.onSuccess(null);
            }
        }, getErrorListener());
        rQueue.add(authorsRequest);
    }

    /**
     * Populates List of Posts.
     * @param postListCallback
     */
    protected void retrievePosts(final PostListCallback postListCallback) {
        final String postsUrl = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + postsPerPage + "&order=desc&orderby=date&fields=id,title,date,author&offset=" + postsOffset;
        StringRequest postsRequest = new StringRequest(Request.Method.GET, postsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray postsJson = new JsonParser().parse(response).getAsJsonArray();
                List<Post> newPosts = new ArrayList<>();
                int count = 0;
                for (JsonElement post : postsJson) {
                    JsonObject postJson = (JsonObject) post;
                    String title = postJson.get("title").getAsJsonObject().get("rendered").getAsString();
                    String date = postJson.get("date").getAsString();
                    int authorId = postJson.get("author").getAsInt();
                    int id = postJson.get("id").getAsInt();
                    String content = postJson.get("content").getAsJsonObject().get("rendered").getAsString();
                    newPosts.add(new Post(title, date, authors.get(authorId), id, content));
                    count++;
                }
                postListCallback.onSuccess(newPosts);
                postsOffset = postsOffset + postsPerPage;
            }
        }, getErrorListener());

        rQueue.add(postsRequest);
    }

    /**
     * Logs error and displays errorMessage dialog.
     */
    protected Response.ErrorListener getErrorListener() {
        Response.ErrorListener responseListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // displays errorMessage
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


    /**
     * Sets the scroll listener for the list view. When the user scrolls to the bottom, calls method to load more posts by the specified increment (postsPerPage)
     */
    private void setScrollListener() {
        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        // ensures new posts are loaded only once per time the bottom is reached (i.e. if the user continuously scrolls to the bottom, more than "postsPerPage" posts will not be loaded
                        if (postListView.getAdapter().getCount() == currentListSize + postsPerPage) {
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

    public AlertDialog getErrorMessage() { return errorMessage; }
    public ListView getPostListView() { return postListView; }

    public List<Post> getPosts() { return posts; }
    public int getPostsPerPage() { return postsPerPage; }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    public interface PostListCallback<T> {
        void onSuccess(List<T> newItem);
    }

}


