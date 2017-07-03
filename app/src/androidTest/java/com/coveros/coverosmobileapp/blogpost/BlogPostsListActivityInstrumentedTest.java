package com.coveros.coverosmobileapp.blogpost;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import com.coveros.coverosmobileapp.R;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class BlogPostsListActivityInstrumentedTest{
    @Rule
    public ActivityTestRule<BlogPostsListActivity> mBlogListRule = new ActivityTestRule<>(BlogPostsListActivity.class);

    @Test
    @UiThreadTest
    public void slideNavigationMenuClosed(){
        DrawerLayout menu = (DrawerLayout) mBlogListRule.getActivity().findViewById(R.id.drawer_layout);
        if(menu.isDrawerOpen(Gravity.START)){
            menu.closeDrawer(Gravity.START);
        }
        assertFalse(menu.isDrawerOpen(Gravity.START));
    }

}