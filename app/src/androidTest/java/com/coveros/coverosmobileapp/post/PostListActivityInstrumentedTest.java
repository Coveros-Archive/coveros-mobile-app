package com.coveros.coverosmobileapp.post;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;
import com.coveros.coverosmobileapp.website.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Maria Kim and Sadie Rynestad
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
    @Test
    public void slideNavigationMenuOpen(){
        DrawerLayout menu = (DrawerLayout) mPostListRule.getActivity().findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
        assertTrue(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    public void slideNavigationMenuClosed(){
        DrawerLayout menu = (DrawerLayout) mPostListRule.getActivity().findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
        assertFalse(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    public void clickWebsiteTab(){
        ListView drawerList = (ListView) mPostListRule.getActivity().findViewById(R.id.left_drawer);
        TextView website = (TextView) drawerList.getItemAtPosition(0);
        website.performClick();
        
    }

    @Test
    public void clickBlogTab(){
        ListView drawerList = (ListView) mPostListRule.getActivity().findViewById(R.id.left_drawer);
        TextView blog = (TextView) drawerList.getItemAtPosition(1);
        blog.performClick();

    }
}



