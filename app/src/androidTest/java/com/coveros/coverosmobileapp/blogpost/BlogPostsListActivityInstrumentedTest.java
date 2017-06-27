package com.coveros.coverosmobileapp.blogpost;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * @author Maria Kim
 */
@RunWith(AndroidJUnit4.class)
public class BlogPostsListActivityInstrumentedTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<BlogPostsListActivity> mBlogPostListRule = new ActivityTestRule<>(BlogPostsListActivity.class);

    @Test
    public void getErrorListener_withPostList() {
        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mBlogPostListRule.getActivity().getErrorListener();
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mBlogPostListRule.getActivity().getErrorMessage().isShowing());
    }

}



