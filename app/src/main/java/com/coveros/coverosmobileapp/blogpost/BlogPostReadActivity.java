package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.errorlistener.ErrorMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 *
 * @author Maria Kim
 * @author Sadie Rynestad
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogPostReadActivity extends AppCompatActivity {
    private static final int NUM_OF_AUTHORS = 100;  // number of users that will be returned by the REST call... so if someday Coveros has over 100 employees, this needs to be changed
    private static final String AUTHORS_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/users?orderby=id&per_page=" + NUM_OF_AUTHORS;
    private SparseArray<String> authors = new SparseArray<>();  // to aggregate the ids and names of the authors of displayed blog posts

    /**
     * Grabs post data from Intent and displays it and its comments.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        final int blogId = getIntent().getIntExtra("blogId", 0);
        final String blogPost = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + blogId;
        final RequestQueue rQueue = Volley.newRequestQueue(BlogPostReadActivity.this);
        retrieveAuthors(new PostListCallback<String>() {
            @Override
            public void onSuccess(List<String> newAuthors) {
                StringRequest blogPostsRequest = new StringRequest(Request.Method.GET, blogPost, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject blogPostsJson = new JsonParser().parse(response).getAsJsonObject();
                        BlogPost post = new BlogPost(blogPostsJson, authors);
                        WebView content = (WebView) findViewById(R.id.content);
                        content.loadData(post.getContent(), "text/html; charset=utf-8", "UTF-8");
                        setTitle(post.getTitle());
                    }
                }, new ErrorMessage(BlogPostReadActivity.this));
                rQueue.add(blogPostsRequest);
            }
        });

        final ImageButton addBookmark = (ImageButton) findViewById(R.id.bookmark_button_unchecked);
        final ImageButton removeBookmark = (ImageButton) findViewById(R.id.bookmark_button_checked);
        final String ids = "";
        File newXml = new File(getFilesDir()+"C:/Users/SRynestad/AndroidStudioProjects/coveros-mobile-app/app/src/main/res/values/ids.xml");
        addBookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try(FileOutputStream fos = openFileOutput(ids, Context.MODE_APPEND)){
                   fos.write(blogId);
                    fos.close();
                    removeBookmark.bringToFront();
                }catch(IOException ex){
                    Log.e("", "Saving bookmark to file caused an error");
                }

            }
        });

        removeBookmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                StringBuilder sb = new StringBuilder();
                try(FileInputStream fis = openFileInput(ids)){
                    Scanner s = new Scanner(fis);
                    while(s.hasNext()){
                        sb.append(s.nextLine());
                        sb.append("\n");
                    }
                    Log.e("THE DATAZ: ", sb.toString());
                    addBookmark.bringToFront();
                }catch(IOException ex){
                    Log.e("", "Removing saved bookmark from file caused an error");
                }


            }
        });

        Button viewComments = (Button) findViewById(R.id.view_comments);
        // when user clicks on "View comments" button, open up CommentsListActivity to display comments for this post
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsListActivity.class);
                intent.putExtra("postId", Integer.toString(blogId));
                startActivity(intent);
            }
        });
    }
    /**
     * Populates List of Authors.
     *
     * @param postListCallback A callback function to be executed after the list of authors has been retrieved
     */
    protected void retrieveAuthors(final PostListCallback<String> postListCallback) {
        RequestQueue rQueue = Volley.newRequestQueue(BlogPostReadActivity.this);
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
        }, new ErrorMessage(BlogPostReadActivity.this));
        rQueue.add(authorsRequest);
    }

    interface PostListCallback<T> {
        void onSuccess(List<T> newItems);
    }
    private static final String ns = null;
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readResources(parser);
        } finally {
            in.close();
        }
    }
    private List readResources(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "resources");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("id")) {
                entries.add(readId(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
