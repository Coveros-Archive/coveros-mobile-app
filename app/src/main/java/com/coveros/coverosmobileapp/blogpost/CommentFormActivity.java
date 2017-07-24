package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String postId;
    AlertDialog emptyFieldAlertDialog;

    private static final String COMMENT_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/comments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);

        postId = getIntent().getExtras().getString("postId");
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        final Pattern emailPattern = Pattern.compile(emailRegex);


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

                Matcher emailMatcher = emailPattern.matcher(email);

                boolean isValidEmail = emailMatcher.matches();

                JsonObject body = new JsonObject();
                body.addProperty("post", postId);
                Log.d("postId", postId);
                body.addProperty("author_name", author);
                Log.d("author_name", author);
                body.addProperty("author_email", email);
                Log.d("author_email", email);
                body.addProperty("content", message);
                Log.d("content", message);
                RestRequest commentRequest = new RestRequest(COMMENT_URL, null, body, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        showSuccessDialog(CommentFormActivity.this);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorDialog(CommentFormActivity.this);
                    }
                });
                Log.d("request type", commentRequest.getRestMethod() + "");
                if (emptyFields.isEmpty() && isValidEmail) {
                    RequestQueue requestQueue = Volley.newRequestQueue(CommentFormActivity.this);
                    requestQueue.add(commentRequest);
                } else if (!emptyFields.isEmpty() && !isFinishing()){
                    emptyFieldAlertDialog = createEmptyFieldAlertDialog(emptyFields);
                    emptyFieldAlertDialog.show();
                } else if (!isValidEmail && !isFinishing()){
                    createInvalidEmailDialog().show();
                }

            }
        });

    }

    void showSuccessDialog(Context context) {
        AlertDialog commentPostedDialog = new AlertDialog.Builder(context).create();
        commentPostedDialog.setTitle(context.getString(R.string.success_dialog_title));
        commentPostedDialog.setMessage(context.getString(R.string.success_dialog_message));
        commentPostedDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.success_dialog_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        commentPostedDialog.show();
    }

    void showErrorDialog(Context context) {
        AlertDialog commentFailedDialog = new AlertDialog.Builder(context).create();
        commentFailedDialog.setTitle(context.getString(R.string.error_dialog_title));
        commentFailedDialog.setMessage(context.getString(R.string.error_dialog_message));
        commentFailedDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.error_dialog_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commentFailedDialog.show();
    }

    private AlertDialog createInvalidEmailDialog () {
        AlertDialog invalidEmailDialog = new AlertDialog.Builder(CommentFormActivity.this).create();
        invalidEmailDialog.setTitle(R.string.invalid_email_dialog_title);
        invalidEmailDialog.setMessage(getResources().getString(R.string.invalid_email_dialog_message));
        invalidEmailDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.invalid_email_dialog_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return invalidEmailDialog;
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
