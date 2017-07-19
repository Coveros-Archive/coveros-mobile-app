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
import static junit.framework.Assert.assertTrue;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class CommentsListActivityInstrumentedTest {

    CommentsListActivity commentsListActivity;

    private static int EXPECTED_HEADER_COUNT = 1;

    @Rule
    public ActivityTestRule<CommentsListActivity> commentsListActivityRule = new ActivityTestRule<CommentsListActivity>(CommentsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", "" + "7509");  // passing id of specific blog post
            return intent;
        }
    };

    @Before
    public void setUp() {
       commentsListActivity = commentsListActivityRule.getActivity();
    }

    @Test
    public void onCreate_checkListViewIsShown() {
        boolean listViewIsShown = commentsListActivity.getCommentsListView().isShown();
        assertTrue("ListView should be shown.", listViewIsShown);
    }

    @Test
    public void onCreate_checkButtonIsShown() {
        boolean buttonIsShown = commentsListActivity.findViewById(R.id.leave_comment).isShown();
        assertTrue("\"Leave a Comment\" button should be shown.", buttonIsShown);
    }

    @Test
    public void onCreate_checkHeaderViewTextViewAdded() {
        int actualHeaderCount = commentsListActivity.getCommentsListView().getHeaderViewsCount();
        assertEquals("One header view should be added", EXPECTED_HEADER_COUNT, actualHeaderCount);
    }

}