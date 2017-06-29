package com.coveros.coverosmobileapp.blogpost;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


/**
 * @author Maria Kim
 */

public class BlogListActivityInstrumentedTest extends LooperTestSuite {

    private static BlogListActivity blogListActivity;

    @Rule
    public ActivityTestRule<BlogListActivity> mBlogListRule = new ActivityTestRule<>(BlogListActivity.class);

    @Before
    public void setUp() {
        blogListActivity = mBlogListRule.getActivity();
    }

    @Test
    public void createTextViewLabel_forComments() {
        TextView commentsLabel = blogListActivity.createTextViewLabel(blogListActivity, "Comments");
        assertEquals("Label should read \"Comments\"", "Comments", commentsLabel.getText());
        assertEquals("Text sizes should match.", blogListActivity.getTextViewTextSize(), commentsLabel.getTextSize());
        assertEquals("Padding bottom should match.", blogListActivity.getTextViewPaddingBottom(), commentsLabel.getPaddingBottom());
    }

    @Test
    public void createErrorMessage_checkAlertDialogNotNull() {
        blogListActivity.errorMessage = blogListActivity.createErrorMessage(blogListActivity);
        assertNotNull("errorMessage should not be null.", blogListActivity.errorMessage);
    }

    @Test
    public void createErrorListener_checkErrorListenerNotNull() {
        blogListActivity.errorListener = blogListActivity.createErrorListener(blogListActivity);
        assertNotNull("errorListener should not be null.", blogListActivity.errorListener);
    }

    @Test
    public void errorListenerOnErrorResposne_withVolleyError() {
        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        blogListActivity.errorListener = blogListActivity.createErrorListener(blogListActivity);
        blogListActivity.errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", blogListActivity.getErrorMessage().isShowing());

    }


}
