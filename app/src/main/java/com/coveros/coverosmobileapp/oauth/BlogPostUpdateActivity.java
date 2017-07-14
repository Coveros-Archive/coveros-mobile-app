package com.coveros.coverosmobileapp.oauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.blogpost.BlogListActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Maria Kim
 */

public class BlogPostUpdateActivity extends AppCompatActivity {

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
                } catch (JSONException e) {
                    Log.e("JSON Exception", e.toString());
                }

                final AlertDialog requestResponse = new AlertDialog.Builder(BlogPostUpdateActivity.this).create();

                RestRequest restRequest = new RestRequest(url, accessToken, body, new RestRequest.Listener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResponse.setTitle(BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_success_title));
                        requestResponse.setMessage(BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_success_message));
                        requestResponse.setButton(AlertDialog.BUTTON_NEUTRAL, BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_dismiss_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        requestResponse.show();
                    }
                }, new RestRequest.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResponse.setTitle(BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_error_title));
                        requestResponse.setMessage(BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_error_message));
                        requestResponse.setButton(AlertDialog.BUTTON_NEUTRAL, BlogPostUpdateActivity.this.getString(R.string.post_update_request_response_dismiss_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        requestResponse.show();
                    }
                });

                Log.d("BODY", restRequest.getBody().toString());
                restRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
                    @Override
                    public void onAuthFailed() {
                        Intent intent = new Intent(getApplicationContext(), BlogPostUpdateActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(BlogPostUpdateActivity.this);
                requestQueue.add(restRequest);


            }
        });
    }

}
