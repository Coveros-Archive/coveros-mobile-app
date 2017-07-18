package com.coveros.coverosmobileapp.website;

import android.content.Intent;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.coveros.coverosmobileapp.test.util.LooperTestSuite;
import com.coveros.coverosmobileapp.test.util.WebResourceRequestImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CustomWebViewClientInstrumentedTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        mainActivityRule.launchActivity(intent);
        mainActivity = mainActivityRule.getActivity();
    }

    /*
     * Check on methods related to PostID
     */
    @Test
    public void shouldOverrideUrlLoading_getPostID_withUrl() {
        final int expectedPostId = 7520;

        WebResourceRequest request = new WebResourceRequestImpl(Uri.parse("https://www3.dev.secureci.com/unity3d-first-class-build-support/"));
        CustomWebViewClient customWebViewClient = mainActivity.getCustomClient();
        WebView view = mainActivity.getWebViewBrowser();
        customWebViewClient.shouldOverrideUrlLoading(view, request);

        int actualPostId = customWebViewClient.getPostId();

        assertThat(actualPostId, equalTo(expectedPostId));
    }

    /*
     * Check on methods related to isBlogPost
     */
//    @Test
//    public void shouldOverrideUrlLoading_checkIsBlogPost_withUrl() {
//        final boolean expectedIsBlogPost = true;
//
//        WebResourceRequest request = new WebResourceRequestImpl(Uri.parse("https://www3.dev.secureci.com/techwell-announces-coveros-ceo-jeffery-payne-agileconnection-technical-editor/"));
//        CustomWebViewClient customWebViewClient = mainActivity.getCustomClient();
//        WebView view = mainActivity.getWebViewBrowser();
//        customWebViewClient.shouldOverrideUrlLoading(view, request);
//
//        boolean actualIsBlogPost = customWebViewClient.getIsBlogPost();
//
//        assertThat(actualIsBlogPost, equalTo(expectedIsBlogPost));
//
//    }

    /*
     * Check Custom Client string for html class name
     */
//    @Test
//    public void shouldOverrideUrlLoading_checkClassName_withUrl() {
//        final String expectedClassName = "post-template-default";
//
//        WebResourceRequest request = new WebResourceRequestImpl(Uri.parse("https://www3.dev.secureci.com/good-free-sequence-diagram-tool"));
//        CustomWebViewClient customWebViewClient = mainActivity.getCustomClient();
//        WebView view = mainActivity.getWebViewBrowser();
//        customWebViewClient.shouldOverrideUrlLoading(view, request);
//
//        String actualClassName = customWebViewClient.getClassName();
//
//        assertThat(actualClassName, equalTo(expectedClassName));
//
//    }

}