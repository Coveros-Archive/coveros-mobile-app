package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

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
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 *
 * @author Maria Kim
 */
public class BlogPostReadActivity extends AppCompatActivity {

    static final int TITLE_KEY = 0;
    static final int CONTENT_KEY = 1;
    static final int ID_KEY = 2;
    private static final String POSTS_URL = "https://www.dev.secureci.com/wp-json/wp/v2/<id>";
    private SparseArray<String> authors = new SparseArray<>();
    private RequestQueue rQueue;

    /**
     * Grabs post data from Intent and displays it and its comments.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final List<String> post = getIntent().getStringArrayListExtra("postData");//change

        WebView content = (WebView) findViewById(R.id.content);

        setTitle(post.get(TITLE_KEY)); //change
        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8"); //change
        Button viewComments = (Button) findViewById(R.id.view_comments);

        // when user clicks on "View comments" button, open up CommentsListActivity to display comments for this post
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsListActivity.class);
                intent.putExtra("postId", "" + post.get(ID_KEY));
                startActivity(intent);
            }
        });

    }


    public void retrieveBlogPosts(final BlogPostsListActivity.PostListCallback<BlogPost> postListCallback){
        StringRequest blogPostsRequest = new StringRequest(Request.Method.GET, String.format(Locale.US, POSTS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray blogPostsJson = new JsonParser().parse(response).getAsJsonArray();
                List<BlogPost> newBlogPosts = new ArrayList<>();
                for (JsonElement blogPost : blogPostsJson) {
                    newBlogPosts.add(new BlogPost((JsonObject) blogPost, authors));
                }
                postListCallback.onSuccess(newBlogPosts);
            }
        }, new BlogPostErrorListener(BlogPostReadActivity.this));

        rQueue.add(blogPostsRequest);
    }
    public class RequestReadThread extends Thread{
        @Override
        public void run(){
            rQueue = Volley.newRequestQueue(BlogPostReadActivity.this);

        }
    }



}
