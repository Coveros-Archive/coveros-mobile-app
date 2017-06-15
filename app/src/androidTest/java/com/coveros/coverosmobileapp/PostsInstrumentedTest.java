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

import java.lang.reflect.Field;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;


/**
 * Created by Maria Kim on 6/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PostsInstrumentedTest {


    @BeforeClass
    public static void setUp() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<Posts> mPostsRule = new ActivityTestRule<Posts>(Posts.class);

    @Test
    public void getErrorListener_withPostList() throws Exception {

        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mPostsRule.getActivity().getErrorListener(mPostsRule.getActivity());
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mPostsRule.getActivity().getErrorMessage().isShowing());
    }

    @Test
    public void onItemClickListener_itemClicked() {

        int position = 0;

        // get ListView of posts and "select" first item
        Posts posts = mPostsRule.getActivity();
        posts.runOnUiThread(new Runnable() {
            public void run() {
                clickItemInPostsListView(posts, position);
            }
        });
        try {
            Activity currentActivity = getActivity();
            Post currentPost = (Post) currentActivity;
            assertTrue(Integer.toString(position).equals(currentPost.getId()));
        }
        catch(Exception e) {
            System.err.println(e);
        }


    }

    public void clickItemInPostsListView(Posts p, int position) {
        Posts posts = p;
        ListView postsListView = posts.getPostsListView();
        postsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        postsListView.setItemChecked(position, true);
    }

    public static Activity getActivity() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        HashMap activities = (HashMap) activitiesField.get(activityThread);
        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }
}
