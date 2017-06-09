package com.coveros.coverosmobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// importing tools for Wordpress integration

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import android.util.Log;


import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    String url = "http://www.coveros.com/wp-json/wp/v2/posts?fields=id,title";
    List<Object> list;
    Gson gson;
    ProgressDialog progressDialog;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    int postID;
    String postTitle[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = (ListView) findViewById(R.id.postList);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitle = new String[list.size()];

                for (int i = 0; i < list.size(); ++i) {
                    mapPost = (Map<String, Object>) list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");
                    postTitle[i] = (String) mapTitle.get("rendered");
                    Log.d("Title: ", postTitle[i]);  // print to debug unicode encoding issue
                }

                postList.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, postTitle));
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if ((volleyError instanceof TimeoutError) || (volleyError instanceof NoConnectionError)) {
                    Toast.makeText(MainActivity.this,
                            "Timeout error",
                            Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    Toast.makeText(MainActivity.this,
                            "Authorization failure error",
                            Toast.LENGTH_LONG).show();

                } else if (volleyError instanceof ServerError) {
                    Toast.makeText(MainActivity.this,
                            "Server error",
                            Toast.LENGTH_LONG).show();

                } else if (volleyError instanceof NetworkError) {
                    Toast.makeText(MainActivity.this,
                            "Network error",
                            Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    Toast.makeText(MainActivity.this,
                            "Parse error",
                            Toast.LENGTH_LONG).show();
                }

            }
        }){
            // trying to figure out unicode encoding...
           @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
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

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapPost = (Map<String, Object>) list.get(position);
                postID = ((Double) mapPost.get("id")).intValue();

                Intent intent = new Intent(getApplicationContext(), Post.class);
                intent.putExtra("id", "" + postID);
                startActivity(intent);
            }
        });

    }
}
