package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
 * Created by Maria on 6/15/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PostInstrumentedTest {

    @BeforeClass
    public static void setUp() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<Post> mPostRule = new ActivityTestRule<Post>(Post.class) {
        @Override
        protected Intent getActivityIntent() {
            Context postContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent postIntent = new Intent(postContext, Post.class);
            postIntent.putExtra("id", "" + 0);
            return postIntent;
        }
    };

    @Test
    public void getErrorListener_withPost() throws Exception {

        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        Response.ErrorListener errorListener = mPostRule.getActivity().getErrorListener(mPostRule.getActivity());
        errorListener.onErrorResponse(volleyError);
        assertTrue(mPostRule.getActivity().getErrorMessage().isShowing());

    }
}
