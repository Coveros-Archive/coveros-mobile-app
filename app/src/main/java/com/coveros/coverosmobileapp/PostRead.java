package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Map;


/**
 * Created by Maria Kim on 6/9/2017.
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 * Reference: https://www.simplifiedcoding.net/wordpress-to-android-app-tutorial/
 */

public class PostRead extends AppCompatActivity {

    static AlertDialog errorMessage;
    TextView title;
    WebView content;

    private boolean isActive;

    private String id;

    /**
     * When post is created (through selection), GETs data for post via Wordpress' REST API and displays title and content.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        id = getIntent().getExtras().getString("id");

        title = (TextView) findViewById(R.id.title);
        content = (WebView) findViewById(R.id.content);

        String url = "https://www.dev.secureci.com/wp-json/wp/v2/posts/" + id + "?fields=author,title,date,content";

        StringRequest request = new StringRequest(Request.Method.GET, url, getListener(), getErrorListener(PostRead.this));

        RequestQueue rQueue = Volley.newRequestQueue(PostRead.this);
        rQueue.add(request);
    }

    public String getId() {
        return id;
    }


    public boolean getIsActive() {
        return isActive;
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

                Log.e("Volley error", "" + volleyError.networkResponse.statusCode);
            }
        };
        return responseListener;
    }
    private Response.Listener<String> getListener() {
        Response.Listener<String> listener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject post = new JsonParser().parse(response).getAsJsonObject();
                JsonObject titleText = post.get("title").getAsJsonObject();
                JsonObject contentText = post.get("content").getAsJsonObject();

                title.setText(StringEscapeUtils.unescapeHtml3(titleText.get("rendered").getAsString()));
                content.loadData(StringEscapeUtils.unescapeHtml3(contentText.get("rendered").getAsString()), "text/html; charset=utf-8", "UTF-8");
            }
        };
        return listener;
    }




}
