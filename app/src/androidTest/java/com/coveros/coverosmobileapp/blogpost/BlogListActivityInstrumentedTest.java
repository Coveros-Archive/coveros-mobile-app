package com.coveros.coverosmobileapp.blogpost;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
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

    private static final String EXPECTED_TEXT_VIEW_LABEL = "Comments";
    private static final float EXPECTED_TEXT_SIZE = blogListActivity.getTextViewTextSize();
    private static final int EXPECTED_PADDING_BOTTOM = blogListActivity.getTextViewPaddingBottom();


    @Rule
    public ActivityTestRule<BlogListActivity> blogListRule = new ActivityTestRule<>(BlogListActivity.class);

    @Before
    public void setUp() {

        blogListActivity = blogListRule.getActivity();

    }

    @Test
    public void createTextViewLabel_forComments() {
        TextView commentsLabel = blogListActivity.createTextViewLabel(blogListActivity, "Comments");

        String actualTextViewLabel = (String) commentsLabel.getText();
        assertEquals("Label should read \"Comments\"", EXPECTED_TEXT_VIEW_LABEL, actualTextViewLabel);

        float actualTextSize = commentsLabel.getTextSize();
        assertEquals("Text sizes should match.", EXPECTED_TEXT_SIZE, actualTextSize);

        int actualPaddingBottom = commentsLabel.getPaddingBottom();
        assertEquals("Padding bottom should match.", EXPECTED_PADDING_BOTTOM, actualPaddingBottom);
    }

    @Test
    public void createErrorMessage_checkAlertDialogNotNull() {
        blogListActivity.networkErrorAlertDialog = blogListActivity.createErrorMessage(blogListActivity);
        assertNotNull("networkErrorAlertDialog should not be null.", blogListActivity.networkErrorAlertDialog);
    }

    @Test
    public void createErrorListener_checkErrorListenerNotNull() {
        blogListActivity.networkErrorListener = blogListActivity.createErrorListener(blogListActivity);
        assertNotNull("networkErrorListener should not be null.", blogListActivity.networkErrorListener);
    }

    @Test
    public void errorListenerOnErrorResponse_withVolleyError() {
        // generate VolleyError to pass into AccessTokenRequestErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        blogListActivity.networkErrorListener = blogListActivity.createErrorListener(blogListActivity);
        blogListActivity.networkErrorListener.onErrorResponse(volleyError);

        boolean errorMessageIsShowing = blogListActivity.getNetworkErrorAlertDialog().isShowing();
        assertTrue("networkErrorAlertDialog should be displayed.", errorMessageIsShowing);

    }



}
