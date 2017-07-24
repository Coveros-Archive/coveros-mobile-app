package com.coveros.coverosmobileapp.blogpost;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.oauth.RestRequest;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a form through which a user can create and send a comment on a blog post.
 *
 * @author Maria Kim
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CommentFormActivity extends AppCompatActivity {
    private String author;
    private String email;
    private String message;
    private AlertDialog emptyFieldAlertDialog;

    private static final String COMMENT_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/comments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);

        final String postId = getIntent().getExtras().getString("postId");

        Button sendMessage = (Button) findViewById(R.id.send_button);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enterName = (EditText) findViewById(R.id.enter_name);
                EditText enterEmail = (EditText) findViewById(R.id.enter_email);
                EditText enterMessage = (EditText) findViewById(R.id.enter_message);
                author = enterName.getText().toString();
                email = enterEmail.getText().toString();
                message = enterMessage.getText().toString();
                List<String> emptyFields = checkFieldIsEmpty(author, email, message);

                JsonObject body = new JsonObject();
                body.addProperty("post", postId);
                body.addProperty("author_name", author);
                body.addProperty("author_email", email);
                body.addProperty("content", message);
                RestRequest commentRequest = new RestRequest(COMMENT_URL, null, body, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("SUCCESS", "COMMENT POSTED");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "COMMENT NOT POSTED");
                    }
                });
                if (emptyFields.isEmpty()) {
                    RequestQueue requestQueue = Volley.newRequestQueue(CommentFormActivity.this);
                    requestQueue.add(commentRequest);
                } else {
                    emptyFieldAlertDialog = createEmptyFieldAlertDialog(emptyFields);
                    if (!isFinishing()) {
                        emptyFieldAlertDialog.show();
                    }
                }

            }
        });

    }


    static List<String> checkFieldIsEmpty(String author, String email, String message) {
        List<String> emptyFields = new ArrayList<>();
        if (author.isEmpty()) {
            emptyFields.add("name");
        }
        if (email.isEmpty()) {
            emptyFields.add("email");
        }
        if (message.isEmpty()) {
            emptyFields.add("message");
        }
        return emptyFields;
    }

    private AlertDialog createEmptyFieldAlertDialog(List<String> emptyFields) {
        emptyFieldAlertDialog = new AlertDialog.Builder(CommentFormActivity.this).create();
        emptyFieldAlertDialog.setTitle(R.string.empty_field_alert_dialog_title);
        String emptyFieldsString;
        if (emptyFields.size() == 1) {
            emptyFieldsString = emptyFields.get(0);
        } else if (emptyFields.size() == 2) {
            emptyFieldsString = emptyFields.get(0) + " and " + emptyFields.get(1);
        } else {
            emptyFieldsString = emptyFields.get(0) + ", " + emptyFields.get(1) + ", and " + emptyFields.get(2);
        }

        emptyFieldAlertDialog.setTitle(getResources().getString(R.string.empty_field_alert_dialog_title));
        emptyFieldAlertDialog.setMessage(getResources().getString(R.string.empty_field_alert_dialog_message) + " " + emptyFieldsString + ".");
        emptyFieldAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.empty_field_alert_dialog_dismiss_message),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return emptyFieldAlertDialog;
    }

    String getAuthor() {
        return author;
    }

    String getEmail() {
        return email;
    }

    String getMessage() {
        return message;
    }

    AlertDialog getEmptyFieldAlertDialog() {
        return emptyFieldAlertDialog;
    }

}
