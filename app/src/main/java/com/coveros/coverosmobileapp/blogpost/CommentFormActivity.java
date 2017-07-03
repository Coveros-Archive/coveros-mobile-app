package com.coveros.coverosmobileapp.blogpost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coveros.coverosmobileapp.R;

/**
 * Displays a form through which a user can create and send a comment on a blog post.
 * @author Maria Kim
 */

public class CommentFormActivity extends AppCompatActivity {
    String author;
    String email;
    String message;

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

                // logging for now until we make the actual request
                Log.d("AUTHOR", author);
                Log.d("EMAIL", email);
                Log.d("MESSAGE", message);

            }
        });

    }

}
