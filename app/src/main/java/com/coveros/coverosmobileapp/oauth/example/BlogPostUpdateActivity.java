package com.coveros.coverosmobileapp.oauth.example;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;
import com.coveros.coverosmobileapp.errorlistener.NetworkErrorListener;
import com.coveros.coverosmobileapp.oauth.RestRequest;
import com.google.gson.JsonObject;

/**
 * Update form that collects the id and new content for the blog post that is to be modified.
 * @author Maria Kim
 */

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogPostUpdateActivity extends AppCompatActivity {
    private String postId;
    private String newContent;
    private String url;

    private AlertDialog successAlertDialog;
    private AlertDialog networkErrorAlertDialog;
    private NetworkErrorListener networkErrorListener;

    private RestRequest restRequest;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_update_form);

        accessToken = getIntent().getStringExtra("accessToken");

        final String successAlertDialogMessage = getString(R.string.blogpost_update_form_success_message);
        successAlertDialog = AlertDialogFactory.createSuccessAlertDialogFinishButton(BlogPostUpdateActivity.this, successAlertDialogMessage);

        final String networkErrorAlertDialogMessage = getString(R.string.blogpost_update_form_network_error_message);
        networkErrorAlertDialog = AlertDialogFactory.createNetworkErrorAlertDialogDefaultButton(BlogPostUpdateActivity.this, networkErrorAlertDialogMessage);
        networkErrorListener = new NetworkErrorListener(BlogPostUpdateActivity.this, networkErrorAlertDialog);

        Button postNewContentButton = (Button) findViewById(R.id.post_button);
        postNewContentButton.setOnClickListener(new PostButtonOnClickListener());
    }

    class PostButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            postId = ((TextView) findViewById(R.id.enter_post_id)).getText().toString();
            newContent = ((TextView) findViewById(R.id.enter_new_content)).getText().toString();
            url = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + postId;

            JsonObject body = new JsonObject();
            body.addProperty("content", newContent);

            restRequest = new RestRequest(url, accessToken, body, new Response.Listener<JsonObject>() {
                @Override
                public void onResponse(JsonObject response) {
                    if (!isFinishing()) {
                        successAlertDialog.show();
                    }
                }
            }, networkErrorListener);

            restRequest.setOnAuthFailedListener(new RestRequest.OnAuthFailedListener() {
                @Override
                public void onAuthFailed() {
                    Intent intent = new Intent(getApplicationContext(), BlogPostUpdateActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            if (postId.isEmpty() || newContent.isEmpty()) {  // if either field is empty, don't send request and show Toast
                Toast emptyField = Toast.makeText(BlogPostUpdateActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT);
                emptyField.show();
            } else {  // if both fields are not empty, make request
                RequestQueue requestQueue = Volley.newRequestQueue(BlogPostUpdateActivity.this);
                requestQueue.add(restRequest);
            }


        }

        String getPostId() {
            return postId;
        }

        String getNewContent() {
            return newContent;
        }

        String getUrl() {
            return url;
        }

        AlertDialog getSuccessAlertDialog() {
            return successAlertDialog;
        }

        AlertDialog getNetworkErrorAlertDialog() {
            return networkErrorAlertDialog;
        }

        RestRequest getRestRequest() {
            return restRequest;
        }

    }
}
