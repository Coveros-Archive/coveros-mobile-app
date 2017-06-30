package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
    public void onCreate_checkListViewIsShown() {
        assertTrue("ListView should be shown.", mCommentsListActivityRule.getActivity().getListView().isShown());
    }

    @Test
    public void onCreate_checkButtonIsShown() {
        assertTrue("\"Leave a Comment\" button should be shown.", mCommentsListActivityRule.getActivity().findViewById(R.id.leave_comment).isShown());
    }

    @Test
    public void onCreate_checkHeaderViewTextViewAdded() {
        assertEquals("One header view should be added", 1, mCommentsListActivityRule.getActivity().getListView().getHeaderViewsCount());
    }

}