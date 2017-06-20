package com.coveros.coverosmobileapp;

import android.app.Activity;

import android.os.Looper;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;



import static junit.framework.Assert.assertTrue;


/**
 * @author Maria Kim
 */
@RunWith(AndroidJUnit4.class)
public class PostListActivityInstrumentedTest {


    @BeforeClass
    public static void setUp() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<PostListActivity> mPostListRule = new ActivityTestRule<PostListActivity>(PostListActivity.class);

    @Test
    public void getErrorListener_withPostList() throws Exception {

        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener();
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mPostListRule.getActivity().getErrorMessage().isShowing());
    }

    public void clickItemInPostListView(PostListActivity p, int position) {
        PostListActivity posts = p;
        ListView postListView = posts.getPostListView();
        postListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        postListView.setItemChecked(position, true);
    }

}



