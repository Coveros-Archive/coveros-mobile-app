//package com.coveros.coverosmobileapp.blogpost;
//
//import android.content.Intent;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.SparseArray;
//
//import com.android.volley.NetworkResponse;
//import com.android.volley.VolleyError;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertTrue;
//
///**
// * @author Maria Kim
// */
//
//@RunWith(AndroidJUnit4.class)
//public class BlogPostsListActivityInstrumentedTest {
//
//    BlogPostsListActivity blogPostsListActivity;
//
//    private static int EXPECTED_HEADER_COUNT = 1;
//
//    @Rule
//    public ActivityTestRule<BlogPostsListActivity> blogPostsListActivityRule = new ActivityTestRule<BlogPostsListActivity>(BlogPostsListActivity.class) {
//        @Override
//        public Intent getActivityIntent() {
//            JsonObject blogJson = new Gson().fromJson("{\"id\": 1234, \"author\": 14, \"date\": \"1911-02-03T00:00:00\", \"content\": {\"rendered\": \"<p>I like to make unfunny puns.&#8212;</p>\"}, \"title\": {\"rendered\": \"&#8220;BlogPost\"}}", JsonObject.class);
//            SparseArray authors = new SparseArray();
//            authors.append(14, "Ryan Kenney");
//            BlogPost blogPost = new BlogPost(blogJson, authors);
//
//            ArrayList<String> blogPostData = new ArrayList<>();
//            blogPostData.add(String.valueOf(blogPost.getId()));
//            blogPostData.add(blogPost.getTitle());
//            blogPostData.add(blogPost.getContent());
//            Intent intent = new Intent();
//            intent.putStringArrayListExtra("blogPostData", blogPostData);
//            return intent;
//        }
//    };
//
//    @Before
//    public void setUp() {
//        blogPostsListActivity = blogPostsListActivityRule.getActivity();
//    }
//
//    @Test
//    public void onCreate_checkListViewIsShown() {
//        boolean listViewIsShown = blogPostsListActivity.getListView().isShown();
//        assertTrue("ListView should be shown.", listViewIsShown);
//    }
//
//    @Test
//    public void onCreate_checkHeaderViewTextViewAdded() {
//        int actualHeaderCount = blogPostsListActivity.getListView().getHeaderViewsCount();
//        assertEquals("One header view should be added", EXPECTED_HEADER_COUNT, actualHeaderCount);
//    }
//
//    @Test
//    public void errorListenerOnErrorResponse_withVolleyError() {
//        // generate VolleyError to pass into AccessTokenRequestErrorListener
//        byte[] data = {0};
//        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
//        VolleyError volleyError = new VolleyError(networkResponse);
//
//        // trigger error and check if error message (an AlertDialog) is displayed
//        blogListActivity.getNetworkErrorListener().onErrorResponse(volleyError);
//
//        boolean errorMessageIsShowing = blogListActivity.getNetworkErrorAlertDialog().isShowing();
//        assertTrue("networkErrorAlertDialog should be displayed.", errorMessageIsShowing);
//
//    }
//
//}