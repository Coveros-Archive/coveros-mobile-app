package com.coveros.coverosmobileapp.blogpost;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


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
    @Test
    public void slideNavigationMenuOpen(){
        DrawerLayout menu = (DrawerLayout) mBlogListRule.getActivity().findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
        Assert.assertTrue(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    public void slideNavigationMenuClosed(){
        DrawerLayout menu = (DrawerLayout) mBlogListRule.getActivity().findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
        assertFalse(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    public void clickWebsiteTab(){
        ListView drawerList = (ListView) mBlogListRule.getActivity().findViewById(R.id.left_drawer);
        TextView website = (TextView) drawerList.getItemAtPosition(0);
        website.performClick();

    }

    @Test
    public void clickBlogTab(){
        ListView drawerList = (ListView) mBlogListRule.getActivity().findViewById(R.id.left_drawer);
        TextView blog = (TextView) drawerList.getItemAtPosition(1);
        blog.performClick();

    }


}
