package com.coveros.coverosmobileapp.oauth.example;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.oauth.RestRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Update form that collects the id and new content for the blog post that is to be modified.
 * @author Maria Kim
 */

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogPostUpdateActivity extends AppCompatActivity {
    private String postId;
    private String newContent;
    private String url;

    private AlertDialog successDialog;
    private AlertDialog errorDialog;

    private RestRequest restRequest;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_update_form);

        accessToken = getIntent().getStringExtra("accessToken");

        Button postNewContentButton = (Button) findViewById(R.id.post_button);

        postNewContentButton.setOnClickListener(new PostButtonOnClickListener());
    }

    class PostButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            postId = ((TextView) findViewById(R.id.enter_post_id)).getText().toString();
            newContent = ((TextView) findViewById(R.id.enter_new_content)).getText().toString();
            url = "https://www3.dev.secureci.com/wp-json/wp/v2/posts/" + postId;

            JSONObject body = new JSONObject();
            try {
                body.put("content", newContent);
            } catch (JSONException e) {
                Log.e("JSON Exception", "JSON Object body not populated", e);
            }

            restRequest = new RestRequest(url, accessToken, body, new RestRequest.RestRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    successDialog = createRestRequestSuccessDialog(BlogPostUpdateActivity.this);
                    successDialog.show();
                }
            }, new RestRequest.RestRequestErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorDialog = createRestRequestErrorDialog(BlogPostUpdateActivity.this);
                    errorDialog.show();
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

            if (postId.isEmpty() || newContent.isEmpty()) {  // if either field is empty, don't send request and show Toast
                Toast emptyField = Toast.makeText(BlogPostUpdateActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT);
                emptyField.show();
            } else {  // if both fields are not empty, make request
                RequestQueue requestQueue = Volley.newRequestQueue(BlogPostUpdateActivity.this);
                requestQueue.add(restRequest);
            }

        }
        /**
         * Creates AlertDialog that is displayed when the request is successful.
         * @param context    context on which to display AlertDialog
         */
        private AlertDialog createRestRequestSuccessDialog(Context context) {
            final String successTitle = context.getString(R.string.post_update_request_response_success_title);
            final String successMessage = context.getString(R.string.post_update_request_response_success_message);
            return createRestRequestDialog(context, successTitle, successMessage);
        }

        /**
         * Creates AlertDialog that is displayed when the request returns an error.
         * @param context    context on which to display AlertDialog
         */
        private AlertDialog createRestRequestErrorDialog(Context context) {
            final String errorTitle = context.getString(R.string.post_update_request_response_error_title);
            final String errorMessage = context.getString(R.string.post_update_request_response_error_message);
            return createRestRequestDialog(context, errorTitle, errorMessage);
        }

    }


    /**
     * Creates an AlertDialog to display the response (success or error) from the REST request.
     * @param context    context on which to display the AlertDialog
     * @param title    title of the AlertDialog
     * @param message    message of the AlertDialog
     * @return    AlertDialog with these data
     */
    AlertDialog createRestRequestDialog(Context context, String title, String message) {
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

    String getPostId() {
        return postId;
    }

    String getNewContent() {
        return newContent;
    }

    String getUrl() {
        return url;
    }

    AlertDialog getSuccessDialog() {
        return successDialog;
    }

    AlertDialog getErrorDialog() {
        return errorDialog;
    }

    RestRequest getRestRequest() {
        return restRequest;
    }

}
