package com.coveros.coverosmobileapp.post;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import java.util.List;


/**
 * Creates and displays a single blog post when it is selected from the list of blog post_list.
 * @author Maria Kim
 */
public class PostReadActivity extends AppCompatActivity {

    static final int HEADING_KEY = 0;
    static final int SUBHEADING_KEY = 1;
    static final int CONTENT_KEY = 2;

    /**
     * Grabs post data from Intent and displays it.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        List<String> post = getIntent().getStringArrayListExtra("postData");

        TextView heading = (TextView) findViewById(R.id.heading);
        TextView subheading = (TextView) findViewById(R.id.subheading);
        WebView content = (WebView) findViewById(R.id.content);

        heading.setText(post.get(HEADING_KEY));
        subheading.setText(post.get(SUBHEADING_KEY));
        content.loadData(post.get(CONTENT_KEY), "text/html; charset=utf-8", "UTF-8");
    }
}
