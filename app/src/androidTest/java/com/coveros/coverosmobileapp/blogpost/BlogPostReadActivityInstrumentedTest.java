package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BlogPostReadActivityInstrumentedTest {

    private static BlogPostReadActivity blogPostReadActivity;
    private static final String EXPECTED_TITLE = "The Ugly Barnacle";

    @Rule
    public ActivityTestRule<BlogPostReadActivity> blogPostReadActivityRule = new ActivityTestRule<BlogPostReadActivity>(BlogPostReadActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            BlogPost blogPost = new BlogPost("0", "Marie Kin", "1911-02-03T00:00:00", "<p>Once upon a time there was an ugly barnacle. He was so ugly that everyone died. The end.</p>", "The Ugly Barnacle");
            ArrayList<String> postData = new ArrayList<>();
            postData.add(Integer.toString(blogPost.getId()));
            postData.add(blogPost.getTitle());
            postData.add(blogPost.getContent());
            intent.putStringArrayListExtra("postData", postData);
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostReadActivity = blogPostReadActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkTitlesMatch() {
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
