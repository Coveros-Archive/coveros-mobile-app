package com.coveros.coverosmobileapp;

import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.commons.text.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;





/**
 * Created by maria on 6/9/2017.
 * Displays content of a single blog post.
 * Referenced https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */

public class Post extends AppCompatActivity {
    TextView title;
    WebView content;
    ProgressDialog progressDialog;
    Gson gson;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    Map<String, Object> mapContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final String id = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);

        progressDialog = new ProgressDialog(Post.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String url = "http://www.coveros.com/wp-json/wp/v2/posts/" + id + "?fields=title,content";

        StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
               gson = new Gson();
               mapPost = (Map<String, Object>) gson.fromJson(s, Map.class);
               mapTitle = (Map<String, Object>) mapPost.get("title");
               mapContent = (Map<String, Object>) mapPost.get("content");

               title.setText(StringEscapeUtils.unescapeHtml4(mapTitle.get("rendered").toString()));
               content.loadData(StringEscapeUtils.unescapeHtml4(mapContent.get("rendered").toString()), "text/html; charset=utf-8", "UTF-8");

               progressDialog.dismiss();
           }
        , volleyError -> {
            progressDialog.dismiss();
            Toast.makeText(Post.this, id, Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Response<String> parseNetworkResponse (NetworkResponse response) {
                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(strUTF8, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);
    }
}
