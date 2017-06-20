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
public class PostListInstrumentedTest {


    @BeforeClass
    public static void setUp() {
        Looper.prepare();
    }

    @Rule
    public ActivityTestRule<PostList> mPostsRule = new ActivityTestRule<PostList>(PostList.class);

    @Test
    public void getErrorListener_withPostList() throws Exception {

        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mPostsRule.getActivity().getErrorListener();
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mPostsRule.getActivity().getErrorMessage().isShowing());
    }

    @Test
    public void onItemClickListener_itemClicked() {

        final int position = 0;

        // get ListView of post_list and "select" first item
        final PostList posts = mPostsRule.getActivity();
        posts.runOnUiThread(new Runnable() {
            public void run() {
                clickItemInPostsListView(posts, position);
            }
        });
        try {
            Activity currentActivity = getActivity();
            PostRead currentPost = (PostRead) currentActivity;
            assertTrue(Integer.toString(position).equals(currentPost.getPosition()));
        }
        catch(Exception e) {
            System.err.println(e);
        }


    }

    public void clickItemInPostsListView(PostList p, int position) {
        PostList posts = p;
        ListView postListView = posts.getPostListView();
        postListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        postListView.setItemChecked(position, true);
    }

    /**
     *  Gets current running activity.
     *  Source: https://androidreclib.wordpress.com/2014/11/22/getting-the-current-activity/
     * @return
     * @throws Exception
     */
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
