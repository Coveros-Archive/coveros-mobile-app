package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class CommentsListActivityInstrumentedTest {

    private static final String POST_ID = "0";

    @Rule
    public ActivityTestRule<CommentsListActivity> mCommentsListActivityRule = new ActivityTestRule<CommentsListActivity>(CommentsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", "" + POST_ID);
            return intent;
        }
    };

    @Before
    public void setUp() {
    }

    @Test
    public void onCreate_checkListViewNotNull() {
        assertNotNull("ListView should not be null.", mCommentsListActivityRule.getActivity().getListView());
    }

    @Test
    public void onCreate_checkHeaderViewTextViewNotNull() {
        assertEquals("Header view should not be null", 1, mCommentsListActivityRule.getActivity().getListView().getHeaderViewsCount());
    }

}