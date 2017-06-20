package com.coveros.coverosmobileapp;

import android.app.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import android.support.test.espresso.core.deps.guava.util.concurrent.Monitor;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.util.ArrayMap;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;


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

        // generate VolleyError to pass into ErrorListener
        byte[] data = {0};
        NetworkResponse networkResponse = new NetworkResponse(401, data, new HashMap<String, String>(), true);
        VolleyError volleyError = new VolleyError(networkResponse);

        // trigger error and check if error message (an AlertDialog) is displayed
        Response.ErrorListener errorListener = mPostListRule.getActivity().getErrorListener();
        errorListener.onErrorResponse(volleyError);
        assertTrue("errorMessage should be displayed.", mPostListRule.getActivity().getErrorMessage().isShowing());
    }

    @Test
    public void onItemClickListener_itemClicked() throws Exception {

        final int position = 0;

        // get ListView of post_list and "select" first item

        mPostListRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                clickItemInPostListView(mPostListRule.getActivity(), position);
            }
        });
        Activity currentActivity = getActivity();
        PostRead currentPost = (PostRead) currentActivity;
        assertTrue(Integer.toString(position).equals(currentPost.getPosition()));

    }

//    @Test
//    public void retrievePosts_withoutWordpressConnection() throws InterruptedException, IOException {
//        System.out.println("IS WORDPRESS AVAILABLE?" + isWordpressReachable(mPostListRule.getActivity()));
//        assumeTrue(!isWordpressReachable(mPostListRule.getActivity()));
//        Thread retrievalTest = new Thread() {
//            public void run() {
//                mPostListRule.getActivity().retrievePosts(new PostList.PostListCallback() {
//                    @Override
//                    public void onSuccess(List newItem) {
//
//                    }
//                });
//            }
//        };
//        retrievalTest.start();
//        assertTrue("errorMessage should be displayed.", mPostListRule.getActivity().getErrorMessage().isShowing());
//    }
//
//    @Test
//    public void retrievePosts_withWordpressConnection() throws InterruptedException, IOException {
//        System.out.println("IS WORDPRESS AVAILABLE?" + isWordpressReachable(mPostListRule.getActivity()));
//        assumeTrue(isWordpressReachable(mPostListRule.getActivity()));
//        final int beforeRetrieveLength = mPostListRule.getActivity().getPosts().size();
//        Thread retrievalTest = new Thread() {
//            public void run() {
//                mPostListRule.getActivity().retrievePosts(new PostList.PostListCallback() {
//                    @Override
//                    public void onSuccess(List newItem) {
//                        int afterRetrieveLength = mPostListRule.getActivity().getPosts().size();
//                        assertEquals(afterRetrieveLength, beforeRetrieveLength + mPostListRule.getActivity().getPostsPerPage());
//                    }
//                });
//            }
//        };
//        retrievalTest.start();
//    }
//
//    public boolean isWordpressReachable(Context context) {
//        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            try {
//                URL urlServer = new URL("wordpress.org");
//                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
//                urlConn.setConnectTimeout(3000); // 3 second timeout
//                urlConn.connect();
//                if (urlConn.getResponseCode() == 200) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (MalformedURLException e1) {
//                return false;
//            } catch (IOException e) {
//                return false;
//            }
//        }
//        return false;
//    }

    public void clickItemInPostListView(PostList p, int position) {
        PostList posts = p;
        ListView postListView = posts.getPostListView();
        postListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        postListView.setItemChecked(position, true);
    }

    public boolean isRunning(String activity, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();

        for (ActivityManager.AppTask task : tasks) {
            if (task.toString().equalsIgnoreCase(activity)) {
                return true;
            }
        }
        return false;
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
        ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
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
