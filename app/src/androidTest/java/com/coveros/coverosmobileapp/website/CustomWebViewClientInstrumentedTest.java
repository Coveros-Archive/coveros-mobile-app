package com.coveros.coverosmobileapp.website;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.WebView;

import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CustomWebViewClientInstrumentedTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        mMainActivity.launchActivity(intent);
        mActivity = mMainActivity.getActivity();
    }

    /*
     * Check on methods related to PostID
     */
    @Test
    public void checkCustomClient_GettingPostID(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        final String expected = "post-template-default";
        final int expectedVal = 7520;
        WebView view = mActivity.getWebViewBrowser();
        customWebViewClient.shouldOverrideUrlLoading(view, "https://www3.dev.secureci.com/unity3d-first-class-build-support/");
        assertEquals(expected, customWebViewClient.getSavedClassName());
        assertEquals(expectedVal, customWebViewClient.getPostID());
        customWebViewClient.setPostID(7777);
        assertEquals(7777, customWebViewClient.getPostID());
    }

    /*
     * Check on methods related to isBlogPost
     */
    @Test
    public void checkCustomClient_GettingIsBlogPost(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        WebView view = mActivity.getWebViewBrowser();
        final boolean isABlogPost = true;
        final boolean notABlogAnymore = false;
        customWebViewClient.shouldOverrideUrlLoading(view, "https://www3.dev.secureci.com/techwell-announces-coveros-ceo-jeffery-payne-agileconnection-technical-editor/");
        assertEquals(isABlogPost, customWebViewClient.getIsBlogPost());
        customWebViewClient.setIsBlogPost(notABlogAnymore);
        assertFalse(notABlogAnymore);
    }

    /*
     * Check Custom Client string for html class name
     */
    @Test
    public void checkCustomClient_HTMLClassName(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        final String expected = "post-template-default";
        WebView view = mActivity.getWebViewBrowser();
        customWebViewClient.shouldOverrideUrlLoading(view,"https://www3.dev.secureci.com/good-free-sequence-diagram-tool/");
        assertEquals(expected, customWebViewClient.getSavedClassName());
        assertTrue(customWebViewClient.getIsBlogPost());
    }

    /*
     * Check Custom Client for Post ID (if Blog Post)
     */
    @Test
    public void checkCustomClient_PostID_True(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        int expected = 7509;
        WebView view = mActivity.getWebViewBrowser();
        customWebViewClient.shouldOverrideUrlLoading(view, "https://www3.dev.secureci.com/good-free-sequence-diagram-tool/");
        assertEquals(expected, customWebViewClient.getPostID());
        assertTrue(customWebViewClient.getIsBlogPost());
    }

    /*
     * Check that webName is set and can be changed with customWebViewClient
     */
    @Test
    public void checkCustomClient_NewWebName(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        String expectedDefault = "https://www3.dev.secureci.com";
        String expectedNew = "https://www.google.com";
        String changeIt = customWebViewClient.getMainActivity().getWebName();
        assertEquals(expectedDefault, changeIt);
        changeIt = "https://www.google.com";
        assertEquals(expectedNew, changeIt);
    }
}