package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    RestRequest commentRequest;
    AlertDialog successDialog;
    AlertDialog errorDialog;
    AlertDialog emptyFieldDialog;
    AlertDialog invalidEmailDialog;

    private static final String COMMENT_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/comments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);

        postId = getIntent().getExtras().getString("postId");

        Button sendMessage = (Button) findViewById(R.id.send_button);
        sendMessage.setOnClickListener(new SendButtonOnClickListener());

    }

    JsonObject createCommentRequestBody(String id, String author, String email, String content) {
        JsonObject body = new JsonObject();
        body.addProperty("post", id);
        body.addProperty("author_name", author);
        body.addProperty("author_email", email);
        body.addProperty("content", content);
        return body;
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

    class SendButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            author = ((EditText) findViewById(R.id.enter_name)).getText().toString();
            email = ((EditText) findViewById(R.id.enter_email)).getText().toString();
            message = ((EditText) findViewById(R.id.enter_message)).getText().toString();
            List<String> emptyFields = checkFieldIsEmpty(author, email, message);

            String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";  // intellij may complain about it, but \\x08 compiles fine.
            final Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email);
            boolean isValidEmail = emailMatcher.matches();

            JsonObject body = createCommentRequestBody(postId, author, email, message);

            commentRequest = new RestRequest(COMMENT_URL, null, body, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    createSuccessDialog(CommentFormActivity.this);
                    successDialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    createErrorDialog(CommentFormActivity.this);
                    errorDialog.show();
                }
            });

            boolean hasNoEmptyFields = emptyFields.isEmpty();
            if (hasNoEmptyFields && isValidEmail) {
                RequestQueue requestQueue = Volley.newRequestQueue(CommentFormActivity.this);
                requestQueue.add(commentRequest);
            } else if (!isFinishing()) {
                if (!hasNoEmptyFields) {
                    createEmptyFieldDialog(emptyFields);
                    emptyFieldDialog.show();
                } else {  // if not a valid email
                    createInvalidEmailDialog();
                    invalidEmailDialog.show();
                }
            }
        }

        private void createSuccessDialog(Context context) {
            successDialog = new AlertDialog.Builder(context).create();
            successDialog.setTitle(context.getString(R.string.success_dialog_title));
            successDialog.setMessage(context.getString(R.string.success_dialog_message));
            successDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.success_dialog_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        }

        private void createErrorDialog(Context context) {
            errorDialog = new AlertDialog.Builder(context).create();
            errorDialog.setTitle(context.getString(R.string.error_dialog_title));
            errorDialog.setMessage(context.getString(R.string.error_dialog_message));
            errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.error_dialog_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        private void createInvalidEmailDialog () {
            invalidEmailDialog = new AlertDialog.Builder(CommentFormActivity.this).create();
            invalidEmailDialog.setTitle(R.string.invalid_email_dialog_title);
            invalidEmailDialog.setMessage(getResources().getString(R.string.invalid_email_dialog_message));
            invalidEmailDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.invalid_email_dialog_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }

        private void createEmptyFieldDialog(List<String> emptyFields) {
            emptyFieldDialog = new AlertDialog.Builder(CommentFormActivity.this).create();
            emptyFieldDialog.setTitle(R.string.empty_field_alert_dialog_title);
            String emptyFieldsString;
            if (emptyFields.size() == 1) {
                emptyFieldsString = emptyFields.get(0);
            } else if (emptyFields.size() == 2) {
                emptyFieldsString = emptyFields.get(0) + " and " + emptyFields.get(1);
            } else {
                emptyFieldsString = emptyFields.get(0) + ", " + emptyFields.get(1) + ", and " + emptyFields.get(2);
            }

            emptyFieldDialog.setMessage(getResources().getString(R.string.empty_field_alert_dialog_message) + " " + emptyFieldsString + ".");
            emptyFieldDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.empty_field_alert_dialog_dismiss_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
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

    RestRequest getCommentRequest() {
        return commentRequest;
    }

    AlertDialog getSuccessDialog() {
        return successDialog;
    }

    AlertDialog getErrorDialog() {
        return errorDialog;
    }

    AlertDialog getInvalidEmailDialog() {
        return invalidEmailDialog;
    }

    AlertDialog getEmptyFieldDialog() {
        return emptyFieldDialog;
    }

}
