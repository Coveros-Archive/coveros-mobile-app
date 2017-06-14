package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AlertDialog;

import com.android.volley.NetworkResponse;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Maria Kim on 6/13/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AbstractPostActivityTest {

    @BeforeClass
    public static void prepareLooper() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<Post> mPostRule = new ActivityTestRule<Post>(Post.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, Post.class);
            result.putExtra("id", "" + 0);
            return result;
        }
    };

    @Rule
    public ActivityTestRule<PostList> mPostListRule = new ActivityTestRule<PostList>(PostList.class);

    @Test
    public void getErrorListener_withPostList() throws Exception {

        VolleyError volleyError = new VolleyError(createNetworkResponse());

        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener(mPostListRule.getActivity());
        errorListener.onErrorResponse(volleyError);
        PostList postList = mPostListRule.getActivity();
        AlertDialog errorMessage = postList.getErrorMessage();
        boolean errorMessaageIsShowing = errorMessage.isShowing();
        assertTrue(mPostListRule.getActivity().getErrorMessage().isShowing());
    }

    @Test
    public void getErrorListener_withPost() throws Exception {

        VolleyError volleyError = new VolleyError(createNetworkResponse());

        Response.ErrorListener errorListener = mPostRule.getActivity().getErrorListener(mPostRule.getActivity());
        errorListener.onErrorResponse(volleyError);
        assertTrue(mPostRule.getActivity().getErrorMessage().isShowing());

    }



    private NetworkResponse createNetworkResponse() {
        byte[] data = {0};
        HashMap<String, String> headers = new HashMap<>();
        headers.put("0", "0");
        boolean notModified = true;

        return new NetworkResponse(401, data, headers, notModified);
    }




}
