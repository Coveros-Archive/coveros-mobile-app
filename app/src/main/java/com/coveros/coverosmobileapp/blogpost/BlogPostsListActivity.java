package com.coveros.coverosmobileapp.blogpost;


import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import java.util.Locale;

/**
 * @author Maria Kim
 * Creates ListView that displays list of titles of blog post_list.
 */
public class BlogPostsListActivity extends BlogListActivity {

    private List<BlogPost> blogPosts = new ArrayList<>();
    private SparseArray<String> authors = new SparseArray<>();  // to aggregate the ids and names of the authors of displayed blog posts
    private RequestQueue rQueue;

    private ListView postListView;
    private BlogPostsListAdapter postsAdapter;

    private int currentListSize;

    private static final int POSTS_PER_PAGE = 10;
    private int postsOffset = 0;

    private static final int NUM_OF_AUTHORS = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    private static final String AUTHORS_URL = "https://www.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + NUM_OF_AUTHORS;

    private static final String POSTS_URL = "https://www.dev.secureci.com/wp-json/wp/v2/posts?per_page=" + POSTS_PER_PAGE + "&order=desc&orderby=date&fields=id,title,date,author&offset=%d";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.post_list);

        postListView = getListView();
        postListView.addHeaderView(createTextViewLabel(BlogPostsListActivity.this, "Blog posts"));  // settings label above blog post list

        errorListener = createErrorListener(BlogPostsListActivity.this);

        // running these requests on a separate thread for performance
        Thread requests = new Thread() {
            @Override
            public void run() {
                rQueue = Volley.newRequestQueue(BlogPostsListActivity.this);
                retrieveAuthors(new PostListCallback<String>() {
                    @Override
                    public void onSuccess(List<String> newAuthors) {
                        retrieveBlogPosts(new PostListCallback<BlogPost>() {
                            @Override
                            public void onSuccess(List<BlogPost> newPosts) {
                                blogPosts.addAll(newPosts);
                                postsAdapter = new BlogPostsListAdapter(BlogPostsListActivity.this, R.layout.post_list_text, blogPosts);
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

        // when a post is selected, feeds its associated data into a BlogPostReadActivity activity
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogPost blogPost = blogPosts.get(position - 1);  // -1 because the TextView offsets the blogPosts by one for some reason
                ArrayList<String> blogPostData = new ArrayList<>();
                blogPostData.add(blogPost.getTitle());
                blogPostData.add(blogPost.getContent());
                blogPostData.add(String.valueOf(blogPost.getId()));
                Intent intent = new Intent(getApplicationContext(), BlogPostReadActivity.class);
                intent.putStringArrayListExtra("postData", blogPostData);
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
     * @param postListCallback A callback function to be executed after the list of blogPosts has been retrieved
     */
    protected void retrieveBlogPosts(final PostListCallback<BlogPost> postListCallback) {
        StringRequest blogPostsRequest = new StringRequest(Request.Method.GET, String.format(Locale.US, POSTS_URL, postsOffset), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray blogPostsJson = new JsonParser().parse(response).getAsJsonArray();
                List<BlogPost> newBlogPosts = new ArrayList<>();
                for (JsonElement blogPost : blogPostsJson) {
                    newBlogPosts.add(new BlogPost((JsonObject) blogPost, authors));
                }
                postListCallback.onSuccess(newBlogPosts);
                postsOffset = postsOffset + POSTS_PER_PAGE;
            }
        }, errorListener);

        rQueue.add(blogPostsRequest);
    }

    /**
     * Sets the scroll listener for the list view. When the user scrolls to the bottom, calls method to load more blogPosts by the specified increment (POSTS_PER_PAGE)
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
                        // ensures new blogPosts are loaded only once per time the bottom is reached (i.e. if the user continuously scrolls to the bottom, more than "POSTS_PER_PAGE" blogPosts will not be loaded
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
     * Adds blogPosts to the list view. Called when user scrolls to the bottom of the listview.
     */
    protected void addPosts() {
        Thread addPostRequest = new Thread() {
            @Override
            public void run() {
                retrieveBlogPosts(new PostListCallback<BlogPost>() {
                    @Override
                    public void onSuccess (List <BlogPost> newPosts) {
                        postsAdapter.addAll(newPosts);
                        postsAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        addPostRequest.start();
    }

    Response.ErrorListener getErrorListener() { return errorListener; }
    AlertDialog getErrorMessage() { return errorMessage; }

    /**
     * Used to ensure StringRequests are completed before their data are used.
     */
    interface PostListCallback<T> {
        void onSuccess(List<T> newItems);
    }
}


