package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;
import android.widget.AdapterView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Maria Kim on 6/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PostListInstrumentedTest {

    @BeforeClass
    public static void setUp() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<PostList> mPostListRule = new ActivityTestRule<PostList>(PostList.class);

    @Test
    public void getErrorListener_withPostList() throws Exception {

        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);d

        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener(mPostListRule.getActivity());
        errorListener.onErrorResponse(volleyError);
        PostList postList = mPostListRule.getActivity();
        AlertDialog errorMessage = postList.getErrorMessage();
        boolean errorMessageIsShowing = errorMessage.isShowing();
        assertTrue(mPostListRule.getActivity().getErrorMessage().isShowing());
    }

//    @Test
//    public void onItemClickListener_itemClicked() {
//        AdapterView.OnItemClickListener onItemClickListener = mPostList.getActivity().setItemOnClick
//    }


}
