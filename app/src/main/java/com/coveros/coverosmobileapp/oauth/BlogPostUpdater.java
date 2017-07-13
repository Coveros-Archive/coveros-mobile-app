package com.coveros.coverosmobileapp.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Maria Kim
 */

public class BlogPostUpdater extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_update_form);

        final String accessToken = getIntent().getStringExtra("accessToken");

        Button postNewContentButton = (Button) findViewById(R.id.post_button);

        postNewContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = ((TextView) findViewById(R.id.enter_post_id)).getText().toString();
                String newContent = ((TextView) findViewById(R.id.enter_new_content)).getText().toString();
                newContent = newContent.replace(" ", "+");
                String url = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + postId;
                JSONObject body = new JSONObject();
                try {
                    body.put("content", newContent);
                } catch(JSONException e) {
                    Log.e("JSON Exception", e.toString());
                }

                RestRequest restRequest = new RestRequest(url, accessToken, body, new RestRequest.Listener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS", response.toString());
                    }
                }, new RestRequest.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                });

                Log.d("BODY",restRequest.getBody().toString());
                restRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
                    @Override
                    public void onAuthFailed() {
                        Intent intent = new Intent(getApplicationContext(), BlogPostUpdater.class);
                        startActivity(intent);
                        finish();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(BlogPostUpdater.this);
                requestQueue.add(restRequest);


            }
        });
    }

}
