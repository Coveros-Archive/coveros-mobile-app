package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.coveros.coverosmobileapp.R;

import java.util.List;


/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 * @author Maria Kim
 */
public class BlogPostReadActivity extends AppCompatActivity {

    static final int HEADING_KEY = 0;
    static final int SUBHEADING_KEY = 1;
    static final int CONTENT_KEY = 2;
    static final int ID_KEY = 4;


    /**
     * Grabs post data from Intent and displays it and its comments.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);


        final List<String> post = getIntent().getStringArrayListExtra("postData");


        WebView content = (WebView) findViewById(R.id.content);

        setTitle(post.get(HEADING_KEY));
        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8");

        Button viewComments = (Button) findViewById(R.id.view_comments);
        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsListActivity.class);
                intent.putExtra("postId", "" + post.get(ID_KEY));
                startActivity(intent);
            }
        });

    }


}
