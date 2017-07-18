package com.coveros.coverosmobileapp.blogpost;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.coveros.coverosmobileapp.R;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);
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
                if (!emptyFields.isEmpty()) {
                    emptyFieldAlertDialog = createEmptyFieldAlertDialog(emptyFields);
                    emptyFieldAlertDialog.show();
                }
                // logging for now until we make the actual request
                Log.d("AUTHOR", author);
                Log.d("EMAIL", email);
                Log.d("MESSAGE", message);
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
        AlertDialog newEmptyFieldAlertDialog = new AlertDialog.Builder(CommentFormActivity.this).create();
        newEmptyFieldAlertDialog.setTitle(R.string.empty_field_alert_dialog_title);
        String emptyFieldsString;
        if (emptyFields.size() == 1) {
            emptyFieldsString = emptyFields.get(0);
        } else if (emptyFields.size() == 2) {
            emptyFieldsString = emptyFields.get(0) + " and " + emptyFields.get(1);
        } else {
            emptyFieldsString = emptyFields.get(0) + ", " + emptyFields.get(1) + ", and " + emptyFields.get(2);
        }

        newEmptyFieldAlertDialog.setTitle(getResources().getString(R.string.empty_field_alert_dialog_title));
        newEmptyFieldAlertDialog.setMessage(getResources().getString(R.string.empty_field_alert_dialog_message) + " " + emptyFieldsString + ".");
        newEmptyFieldAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.empty_field_alert_dialog_dismiss_message),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return newEmptyFieldAlertDialog;
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
