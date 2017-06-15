//package com.coveros.coverosmobileapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Looper;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.v7.app.AlertDialog;
//
//import com.android.volley.NetworkResponse;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//
//import java.util.HashMap;
//
//import static junit.framework.Assert.assertTrue;
//
///**
// * Created by Maria Kim on 6/13/2017.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class AbstractPostActivityTest {
//
//    @BeforeClass
//    public static void prepareLooper() {
//        Looper.prepare();
//    }
//
//
//
//    @Rule
//    public ActivityTestRule<Post> mPostRule = new ActivityTestRule<Post>(Post.class) {
//        @Override
//        protected Intent getActivityIntent() {
//            Context postContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//            Intent postIntent = new Intent(postContext, Post.class);
//            postIntent.putExtra("id", "" + 0);
//            return postIntent;
//        }
//    };
//
//
//
//    @Test
//    public void getErrorListener_withPostList() throws Exception {
//
//        VolleyError volleyError = new VolleyError(createNetworkResponse());
//
//        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener(mPostListRule.getActivity());
//        errorListener.onErrorResponse(volleyError);
//        PostList postList = mPostListRule.getActivity();
//        AlertDialog errorMessage = postList.getErrorMessage();
//        boolean errorMessaageIsShowing = errorMessage.isShowing();
//        assertTrue(mPostListRule.getActivity().getErrorMessage().isShowing());
//    }
//
//    @Test
//    public void getErrorListener_withPost() throws Exception {
//
//        VolleyError volleyError = new VolleyError(createNetworkResponse());
//
//        Response.ErrorListener errorListener = mPostRule.getActivity().getErrorListener(mPostRule.getActivity());
//        errorListener.onErrorResponse(volleyError);
//        assertTrue(mPostRule.getActivity().getErrorMessage().isShowing());
//
//    }
//
//
//
//
//
////
////
////}
