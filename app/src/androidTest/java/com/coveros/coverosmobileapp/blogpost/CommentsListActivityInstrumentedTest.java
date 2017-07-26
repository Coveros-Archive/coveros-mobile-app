package com.coveros.coverosmobileapp.blogpost;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Maria Kim
 */
@RunWith(AndroidJUnit4.class)
public class CommentsListActivityInstrumentedTest {

    private CommentsListActivity commentsListActivity;

    @Rule
    public ActivityTestRule<CommentsListActivity> commentsListActivityRule = new ActivityTestRule<CommentsListActivity>(CommentsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", "7509");  // passing id of specific blog post
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
        assertThat(listViewIsShown, is(true));
    }

    @Test
    public void onCreate_checkButtonIsShown() {
        boolean buttonIsShown = commentsListActivity.findViewById(R.id.leave_comment).isShown();
        assertThat(buttonIsShown, is(true));
    }

    @Test
    public void onCreate_checkHeaderViewTextViewAdded() {
        final int expectedHeaderCount = 1;
        int actualHeaderCount = commentsListActivity.getCommentsListView().getHeaderViewsCount();
        assertThat(actualHeaderCount, equalTo(expectedHeaderCount));
    }

}