package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class BlogPostsListActivityInstrumentedTest {

    BlogPostsListActivity blogPostsListActivity;

    private static int EXPECTED_HEADER_COUNT = 1;

    @Rule
    public ActivityTestRule<BlogPostsListActivity> blogPostsListActivityRule = new ActivityTestRule<BlogPostsListActivity>(BlogPostsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            BlogPost blogPost = new BlogPost("0", "Marie Kin", "1911-02-03T00:00:00", "<p>Once upon a time there was an ugly barnacle. He was so ugly that everyone died. The end.</p>", "The Ugly Barnacle");
            ArrayList<String> blogPostData = new ArrayList<>();
            blogPostData.add(String.valueOf(blogPost.getId()));
            blogPostData.add(blogPost.getTitle());
            blogPostData.add(blogPost.getContent());
            Intent intent = new Intent();
            intent.putStringArrayListExtra("blogPostData", blogPostData);
            return intent;
        }
    };

    @Before
    public void setUp() {
        blogPostsListActivity = blogPostsListActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkListViewIsShown() {
        boolean listViewIsShown = blogPostsListActivity.getListView().isShown();
        assertTrue("ListView should be shown.", listViewIsShown);
    }

    @Test
    public void onCreate_checkHeaderViewTextViewAdded() {
        int actualHeaderCount = blogPostsListActivity.getListView().getHeaderViewsCount();
        assertEquals("One header view should be added", EXPECTED_HEADER_COUNT, actualHeaderCount);
    }

}