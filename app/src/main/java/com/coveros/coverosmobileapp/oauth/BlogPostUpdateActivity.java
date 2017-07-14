package com.coveros.coverosmobileapp.oauth;

import android.content.Context;
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
import com.coveros.coverosmobileapp.blogpost.BlogPostsListActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Update form that collects the id and new content for the blog post that is to be modified.
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

                RestRequest restRequest = new RestRequest(url, accessToken, body, new RestRequest.Listener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        createSuccessResponse(BlogPostUpdateActivity.this);
                    }
                }, new RestRequest.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createErrorResponse(BlogPostUpdateActivity.this);
                    }
                });

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

    /**
     * Creates AlertDialog that is displayed when the request is successful.
     * @param context    context on which to display AlertDialog
     */
    private void createSuccessResponse(Context context) {
        final String successTitle = context.getString(R.string.post_update_request_response_success_title);
        final String successMessage = context.getString(R.string.post_update_request_response_success_message);
        createRequestResponse(context, successTitle, successMessage);
    }

    /**
     * Creates AlertDialog that is displayed when the request returns an error.
     * @param context    context on which to display AlertDialog
     */
    private void createErrorResponse(Context context) {
        final String errorTitle = context.getString(R.string.post_update_request_response_error_title);
        final String errorMessage = context.getString(R.string.post_update_request_response_error_message);
        createRequestResponse(context, errorTitle, errorMessage);
    }

    /**
     * Creates an AlertDialog to display the response (success or error) from the REST request.
     * @param context    context on which to display the AlertDialog
     * @param title    title of the AlertDialog
     * @param message    message of the AlertDialog
     * @return    AlertDialog with these data
     */
    AlertDialog createRequestResponse(Context context, String title, String message) {
        final String buttonText = context.getString(R.string.post_update_request_response_dismiss_button);
        AlertDialog requestResponse = new AlertDialog.Builder(context).create();
        requestResponse.setTitle(title);
        requestResponse.setMessage(message);
        requestResponse.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return requestResponse;
    }

}
