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
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;  // to decode decimal unicode in strings received from Wordpress


public class MainActivity extends AppCompatActivity {

    String url = "http://www.coveros.com/wp-json/wp/v2/posts?fields=id,title";
    List<Object> list;
    Gson gson;
    ProgressDialog progressDialog;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    int postID;
    String postTitles[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = (ListView) findViewById(R.id.postList);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // GETs list of post titles and displays in a ListView

        StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitles = new String[list.size()];

                for (int i = 0; i < list.size(); ++i) {
                    mapPost = (Map<String, Object>) list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");
                    postTitles[i] = StringEscapeUtils.unescapeHtml4(mapTitle.get("rendered").toString());
                }

                postList.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, postTitles));
                progressDialog.dismiss();
            }
        , volleyError -> {
                // provides more detailed description of thrown error
                if ((volleyError instanceof TimeoutError) || (volleyError instanceof NoConnectionError)) {
                    Toast.makeText(MainActivity.this,
                            "Timeout or no connection error",
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
        });

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
