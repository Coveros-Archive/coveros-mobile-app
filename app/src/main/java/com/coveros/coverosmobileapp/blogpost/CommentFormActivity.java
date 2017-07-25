package com.coveros.coverosmobileapp.blogpost;

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
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;
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
    AlertDialog successAlertDialog;
    AlertDialog networkErrorAlertDialog;
    AlertDialog emptyFieldAlertDialog;
    AlertDialog invalidEmailAlertDialog;

    String emptyFieldAlertDialogMessage;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private static final String COMMENT_URL = "https://www3.dev.secureci.com/wp-json/wp/v2/comments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);

        postId = getIntent().getExtras().getString("postId");

        final String successAlertDialogMessage = getString(R.string.comment_form_success_message);
        successAlertDialog = AlertDialogFactory.createSuccessAlertDialogFinishButton(CommentFormActivity.this, successAlertDialogMessage);

        final String errorAlertDialogMessage = getString(R.string.comment_form_network_error_message);
        networkErrorAlertDialog = AlertDialogFactory.createNetworkErrorAlertDialogDefaultButton(CommentFormActivity.this, errorAlertDialogMessage);

        emptyFieldAlertDialogMessage = getString(R.string.comment_form_empty_field_error_message);
        emptyFieldAlertDialog = AlertDialogFactory.createErrorAlertDialogDefaultButton(CommentFormActivity.this, emptyFieldAlertDialogMessage);

        final String invalidEmailAlertDialogMessage = getString(R.string.comment_form_invalid_email_error_message);
        invalidEmailAlertDialog = AlertDialogFactory.createErrorAlertDialogDefaultButton(CommentFormActivity.this, invalidEmailAlertDialogMessage);

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

            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
            boolean isValidEmail = emailMatcher.matches();

            JsonObject body = createCommentRequestBody(postId, author, email, message);

            commentRequest = new RestRequest(COMMENT_URL, null, body, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    successAlertDialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    networkErrorAlertDialog.show();
                }
            });

            boolean hasNoEmptyFields = emptyFields.isEmpty();
            if (hasNoEmptyFields && isValidEmail) {
                RequestQueue requestQueue = Volley.newRequestQueue(CommentFormActivity.this);
                requestQueue.add(commentRequest);
            } else if (!isFinishing()) {
                if (!hasNoEmptyFields) {
                    emptyFieldAlertDialog.setMessage(emptyFieldAlertDialogMessage + createListOfEmptyFields(emptyFields));
                    emptyFieldAlertDialog.show();
                } else {  // if not a valid email
                    invalidEmailAlertDialog.show();
                }
            }
        }

        private String createListOfEmptyFields(List<String> emptyFields) {
            String emptyFieldString;
            if (emptyFields.size() == 1) {
                emptyFieldString = emptyFields.get(0) + ".";
            } else if (emptyFields.size() == 2) {
                emptyFieldString = emptyFields.get(0) + " and " + emptyFields.get(1) + ".";
            } else {
                emptyFieldString = emptyFields.get(0) + ", " + emptyFields.get(1) + ", and " + emptyFields.get(2) + ".";
            }

            return " Please provide your " + emptyFieldString;
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

    AlertDialog getSuccessAlertDialog() {
        return successAlertDialog;
    }

    AlertDialog getNetworkErrorDialog() {
        return networkErrorAlertDialog;
    }

    AlertDialog getInvalidEmailAlertDialog() {
        return invalidEmailAlertDialog;
    }

    AlertDialog getEmptyFieldAlertDialog() {
        return emptyFieldAlertDialog;
    }

}
