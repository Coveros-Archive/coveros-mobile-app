package com.coveros.coverosmobileapp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;

import org.junit.Rule;
import android.support.test.rule.ActivityTestRule;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import android.support.test.runner.AndroidJUnit4;

import java.util.ArrayList;

import org.junit.Assert;
import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;


/**
 * Created by Maria Kim on 6/13/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AbstractPostActivityTest {
    @Rule
    public ActivityTestRule<Post> mPostRule = new ActivityTestRule<>(Post.class);

    NetworkError networkError;
    NoConnectionError noConnectionError;
    ParseError parseError;
    ServerError serverError;
    TimeoutError timeoutError;


    @Before
    public void setUp() {
//        post = new Post();
//        postList = new PostList();
    }

    @Test
    public void getErrorListener_withAuthFailureError() throws Exception {
        AuthFailureError authFailureError = new AuthFailureError();
//        if (post == null) System.out.println("ERROR: POST IS NULL");
//        if (authFailureError == null) System.out.println("ERROR: AUTHERROR IS NULL");

        mPostRule.getActivity().getErrorListener(mPostRule.getActivity()).onErrorResponse(authFailureError);
        assertTrue(mPostRule.getActivity().getErrorMessage().isShowing());

    }


//
//    @Test
//    public void getErrorListener_withAuthFailureError() {
//
//    }
//
//    @Test
//    public void getErrorListener_withNetworkError() {
//
//       }
//
//    @Test
//    public void getErrorListener_withParseError() {
//
//    }
//
//    @Test
//    public void getErrorListener_withServerError() {
//
//    }
//
//    @Test
//    public void getErrorListener_withTimeoutError() {
//
//    }


}
