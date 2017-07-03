package com.coveros.coverosmobileapp.blogpost;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;
import com.coveros.coverosmobileapp.R;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;


import static org.junit.Assert.assertFalse;

public class BlogPostsListActivityInstrumentedTest{
    @Rule
    public ActivityTestRule<BlogPostsListActivity> mBlogListRule = new ActivityTestRule<>(BlogPostsListActivity.class);
    @Test
    @UiThreadTest
    public void slideNavigationMenuOpen(){
        DrawerLayout menu = (DrawerLayout) mBlogListRule.getActivity().findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
        Assert.assertTrue(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    @UiThreadTest
    public void slideNavigationMenuClosed(){
        DrawerLayout menu = (DrawerLayout) mBlogListRule.getActivity().findViewById(R.id.drawer_layout);
        if(menu.isDrawerOpen(Gravity.START)){
            menu.closeDrawer(Gravity.START);
        }
        assertFalse(menu.isDrawerOpen(Gravity.START));
    }
    @Test
    public void clickWebsiteTab(){
        ListView drawerList = (ListView) mBlogListRule.getActivity().findViewById(R.id.left_drawer);
        TextView website = (TextView) drawerList.getItemAtPosition(0);
        website.performClick();

    }

    @Test
    public void clickBlogTab(){
        ListView drawerList = (ListView) mBlogListRule.getActivity().findViewById(R.id.left_drawer);
        TextView blog = (TextView) drawerList.getItemAtPosition(1);
        blog.performClick();

    }
}