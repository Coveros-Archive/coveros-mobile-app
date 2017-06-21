package com.coveros.coverosmobileapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.widget.ListView;

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
public class PostListActivityInstrumentedTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<PostListActivity> mPostListRule = new ActivityTestRule<>(PostListActivity.class);

    @Test
    public void getErrorListener_withPostList() {
        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener();
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mPostListRule.getActivity().getErrorMessage().isShowing());
    }

    public void clickItemInPostListView(PostListActivity posts, int position) {
        ListView postListView = posts.getPostListView();
        postListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        postListView.setItemChecked(position, true);
    }

}



