package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.SparseArray;
import android.webkit.WebView;

import com.coveros.coverosmobileapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlogPostReadActivityInstrumentedTest {

    private static BlogPostReadActivity blogPostReadActivity;

    private static final String EXPECTED_TITLE = "Scripting with OWASP ZAP";

    @Rule
    public ActivityTestRule<BlogPostReadActivity> blogPostReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("blogId", 6674);
            SparseArray<String> authors = new SparseArray<>();
            authors.append(42, "Ryan Kenny");
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostReadActivity = blogPostReadActivityRule.getActivity();

    }

    @Test
    public void onCreate_checkTitlesMatch() throws InterruptedException {
        Thread.sleep(3000);
        String actualTitle = (String) blogPostReadActivity.getTitle();
        assertEquals("Titles should match", EXPECTED_TITLE, actualTitle);
    }

    @Test
    public void onCreate_checkWebViewContentIsShown() {
        boolean webViewContentIsShown = blogPostReadActivity.findViewById(R.id.content).isShown();
        assertTrue("Content should be displayed", webViewContentIsShown);
    }

    @Test
    public void onCreate_checkViewCommentsButtonIsShown() {
        boolean viewCommentsButtonIsShown = blogPostReadActivity.findViewById(R.id.view_comments).isShown();
        assertTrue("Content should be displayed", viewCommentsButtonIsShown);
    }


}
