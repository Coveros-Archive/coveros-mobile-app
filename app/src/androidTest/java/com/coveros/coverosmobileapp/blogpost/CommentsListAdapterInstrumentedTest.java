package com.coveros.coverosmobileapp.blogpost;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Html;
import android.view.View;
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

    private CommentsListAdapter commentsListAdapter;
    List<Comment> comments = new ArrayList<>();
    private Comment comment;

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

        comment = new Comment("Ryan Kenney", "2017-06-27T10:23:18", "<p>I made Sadie get the rotisserie chicken.</p>");
        comments.add(comment);
        commentsListAdapter = new CommentsListAdapter(mCommentsListActivityRule.getActivity(), R.layout.comment_list_text, comments);
    }


    @Test
    public void getItem_checkName() {
        assertEquals("Ryan Kenney is expected.", comments.get(0).getAuthor(),
                ((Comment) commentsListAdapter.getItem(0)).getAuthor());
    }

    @Test
    public void getItem_checkId() {
        assertEquals("id 0 expected.", 0, commentsListAdapter.getItemId(0));
    }

    @Test
    public void getCount_withOneItem() {
        assertEquals("Count of 1 expected..", 1, commentsListAdapter.getCount());
    }

    @Test
    public void getView_checkThreeTextViewsNotNull() {
        View view = commentsListAdapter.getView(0, null, mCommentsListActivityRule.getActivity().getCommentsListView());

        TextView name = (TextView) view.findViewById(R.id.comment_name);
        TextView date = (TextView) view.findViewById(R.id.comment_date);
        TextView content = (TextView) view.findViewById(R.id.comment_content);

        assertNotNull("View should not be null. ", view);
        assertNotNull("Name TextView should not be null. ", name);
        assertNotNull("Date TextView should not be null. ", date);
        assertNotNull("Content ImageView should not be null. ", content);

        assertEquals("Names should match.", comment.getAuthor(), name.getText());
        assertEquals("Dates should match.", comment.getDate(), date.getText());
        assertEquals("Content should match.", Html.fromHtml(comment.getContent()), content.getText());
    }



}