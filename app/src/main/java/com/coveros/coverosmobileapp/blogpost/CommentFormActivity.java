package com.coveros.coverosmobileapp.blogpost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coveros.coverosmobileapp.R;

/**
 * Displays a form through which a user can create and send a comment on a blog post.
 * @author Maria Kim
 */

public class CommentFormActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_form);
    }

}
