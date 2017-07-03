package com.coveros.coverosmobileapp.blogpost;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coveros.coverosmobileapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Maria Kim
 */

@RunWith(AndroidJUnit4.class)
public class CommentsListAdapterInstrumentedTest {

    private static CommentsListAdapter commentsListAdapter;
    private static CommentsListActivity commentsListActivity;

    private static int EXPECTED_ID = 0;
    private static int EXPECTED_COUNT = 1;
    private static String EXPECTED_NAME = "Ryan Kenney";
    private static String EXPECTED_DATE = "Jun 27, 2017";
    private static String EXPECTED_CONTENT = "\u201CI made Sadie get the rotisserie chicken.";

    @Rule
    public ActivityTestRule<CommentsListActivity> mCommentsListActivityRule = new ActivityTestRule<CommentsListActivity>(CommentsListActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("postId", "0");
            return intent;
        }
    };

    @Before
    public void setUp() {

        commentsListActivity = mCommentsListActivityRule.getActivity();

        Comment comment = new Comment("Ryan Kenney", "2017-06-27T10:23:18", "&#8220;I made Sadie get the rotisserie chicken.");

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        commentsListAdapter = new CommentsListAdapter(commentsListActivity, R.layout.comment_list_text, comments);

    }

    @Test
    public void getItem_checkName() {
        String actualName = ((Comment) commentsListAdapter.getItem(0)).getAuthor();
        assertEquals("Ryan Kenney is expected.", EXPECTED_NAME, actualName);
    }

    @Test
    public void getItem_checkId() {
        int actualid = (int) commentsListAdapter.getItemId(0);
        assertEquals("id 0 expected.", EXPECTED_ID, actualid);
    }

    @Test
    public void getCount_withOneItem() {
        int actualCount = commentsListAdapter.getCount();
        assertEquals("Count of 1 expected..", EXPECTED_COUNT, actualCount);
    }

    @Test
    public void getView_withNullConvertView() {
        View view = commentsListAdapter.getView(0, null, commentsListActivity.getCommentsListView());

        TextView name = (TextView) view.findViewById(R.id.comment_name);
        TextView date = (TextView) view.findViewById(R.id.comment_date);
        TextView content = (TextView) view.findViewById(R.id.comment_content);

        assertNotNull("View should not be null. ", view);
        assertNotNull("Name TextView should not be null. ", name);
        assertNotNull("Date TextView should not be null. ", date);
        assertNotNull("Content ImageView should not be null. ", content);

        String actualName = name.getText().toString();
        assertEquals("Names should match.", EXPECTED_NAME, actualName);

        String actualDate = date.getText().toString();
        assertEquals("Dates should match.", EXPECTED_DATE, actualDate);

        String actualContent = content.getText().toString();
        assertEquals("Content should match.", EXPECTED_CONTENT, actualContent);
    }

    @Test
    public void getView_withNonNullConvertView() {

        ViewGroup convertView = new LinearLayout(commentsListActivity);
        CommentsListAdapter.CommentHolder mCommentHolder = new CommentsListAdapter.CommentHolder();
        mCommentHolder.commentName = new TextView(commentsListActivity);
        mCommentHolder.commentDate = new TextView(commentsListActivity);
        mCommentHolder.commentContent = new TextView(commentsListActivity);
        convertView.setTag(mCommentHolder);

        View view = commentsListAdapter.getView(0, convertView, commentsListActivity.getCommentsListView());

        String actualName = commentsListAdapter.getCommentHolder().commentName.getText().toString();
        assertEquals("Names should match.", EXPECTED_NAME, actualName);

        String actualDate = commentsListAdapter.getCommentHolder().commentDate.getText().toString();
        assertEquals("Dates should match.", EXPECTED_DATE, actualDate);

        String actualContent = commentsListAdapter.getCommentHolder().commentContent.getText().toString();
        assertEquals("Content should match.", EXPECTED_CONTENT, actualContent);

    }



}