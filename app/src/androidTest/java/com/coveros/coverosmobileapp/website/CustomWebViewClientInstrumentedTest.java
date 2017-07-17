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
     * Check Custom Client for Post ID (Not Blog Post)
     * No Post ID (Value: 0)
     * Not a Blog Post

    TEST GETTING HUNG UP / STALLED BY LOADING ARTICLE AND REQUESTING WHICH BROWSER TO OPEN WITH
    @Test
    public void checkCustomClient_PostID_False(){
        CustomWebViewClient customWebViewClient = mActivity.getCustomClient();
        WebView view = mActivity.getWebViewBrowser();
        customWebViewClient.shouldOverrideUrlLoading(view, "https://www.washingtonpost.com/news/act-four/wp/2017/07/16/game-of-thrones-" +
                "season-7-episode-1-review-dragonstone/?hpid=hp_no-name_opinion-card-d%3Ahomepage%2Fstory&utm_term=.0e85f12e5fcd");
        assertEquals(0, customWebViewClient.getPostID());
        assertFalse(customWebViewClient.getIsBlogPost());
    }
    */


    /*
     * Check Custom Client if Post ID exists, check blog post from BlogPostReadActivity
     */
    @Test
    public void checkCustomClient_BlogPostReadActivityOpens(){
        //customWebViewClient.shouldOverrideUrlLoading(view, "https://www3.dev.secureci.com/mobile-app-security-testing-igoat-installation/");
        //assertEquals(BlogPostReadActivity.class, view.getContext());      - View.getContext() doesn't properly (Thread conflict)
    }

    /*
     * Check Custom Client loaded URL contains keyword "blog" loads BlogPostListActivity
     */
    @Test
    public void checkCustomClient_BlogPostListActivityOpens(){

    }

    /*
     * Check Custom Client loaded URL contains keyword "/category/blogs/"
     */
    @Test
    public void checkCustomClient_LoadsBlogPostListActivity_CategoryBlogs(){

    }

    /*
     * Check Custom Client loaded URL contains coveros related content
     */
    @Test
    public void checkCustomClient_CoverosURL(){

    }

    /*
     * Check Custom Client loaded URL contains dev site url
     */
    @Test
    public void checkCustomClient_SecureCI(){

    }

    /*
     * Check Custom Client loaded external content (Use Browser)
     */
    public void checkCustomClient_ExternalContent(){

    }

    /*
     * Check on Page Started
     * User clicks on new page. Loading as expected
     */
    @Test
    public void checkCustomClient_onPageStarted(){
        //Can't test right now - uses view in onBackPressed and webview conflict
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

    /*
     * Check that onReceivedError loads error page && errorFound
     */
    @Test
    public void checkCustomClient_NewError(){
        //Can't test right now - Uses view, WebResourceRequest request, WebResourceError Error
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}